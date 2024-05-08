package com.dp.notes._temp.screenshot

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.database.ContentObserver
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.MediaStore.Images.ImageColumns
import android.provider.MediaStore.MediaColumns
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isInvisible
import com.dp.core.CoreManager
import com.dp.core.manager.ActivityStackManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.LazyThreadSafetyMode.NONE


/**
 * author Dq
 * date on 2023/2/17
 * description 系统截屏/录屏监听管理类
 *             非系统的监听不了......
 */
class ScreenShotManager private constructor() {
    //主线程,用于运行监听器回调
    private val uiHandler by lazy(NONE) { Handler(Looper.getMainLooper()) }

    //系统截图/录屏完成后的回调
    private var completeResult: ((ScreenResBean) -> Unit)? = null
    private var storageApplyResult: ((Uri) -> Unit)? = null

    //记录截图回调的路径,防止系统多次回调,最多记录10条
    private val callbackPaths = ArrayList<String>()

    private var watermarkView: TextView? = null

    /**
     * 注册各个监听,适配
     */
    fun registerListener(completeResult: (ScreenResBean) -> Unit, storageApplyResult: (Uri) -> Unit) {
        this.completeResult = completeResult
        this.storageApplyResult = storageApplyResult

        //创建外部存储器内容观察者,开启监听
        val externalObserver = MediaContentObserver(uiHandler)
        //注册 图片观察者
        CoreManager.app.contentResolver.registerContentObserver(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            VERSION.SDK_INT >= VERSION_CODES.Q,
            externalObserver
        )
        //注册 视频观察者
        CoreManager.app.contentResolver.registerContentObserver(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            VERSION.SDK_INT >= VERSION_CODES.Q,
            externalObserver
        )
    }

    /**
     * 读取媒体数据库(截图/录屏)时需要读取数据: 地址,文件可见性
     * 适配Android10+文件可见性
     * 日期,宽,高 -->不在获取有的机型获取不到
     */
    private fun projections(): Array<String> = if (VERSION.SDK_INT >= VERSION_CODES.Q) {
        arrayOf(ImageColumns.DATA, ImageColumns._ID, ImageColumns.IS_PENDING)
    } else {
        arrayOf(ImageColumns.DATA, ImageColumns._ID)
    }

    /**
     * 收到媒体数据库的内容改变的通知,查询最新一条媒体数据
     */
    fun handleMediaContentChange(activity: Activity, uri: Uri, uid: String, hostId: String) {
        //查询数据,放到try catch里面,防止通知的Uri is hidden Api
        runCatching {
            val cursor = if (VERSION.SDK_INT >= VERSION_CODES.O) {
                CoreManager.app.contentResolver.query(
                    uri, projections(),
                    bundleOf(
                        //查询数量 1条
                        ContentResolver.QUERY_ARG_LIMIT to 1,
                        //排序:倒序
                        ContentResolver.QUERY_ARG_SORT_DIRECTION to ContentResolver.QUERY_SORT_DIRECTION_DESCENDING,
                        //倒序条件:根据添加时间
                        ContentResolver.QUERY_ARG_SORT_COLUMNS to arrayOf(MediaStore.Files.FileColumns.DATE_ADDED)
                    ),
                    null
                )
            } else {
                CoreManager.app.contentResolver.query(
                    uri, projections(),
                    null, null,
                    MediaColumns.DATE_ADDED + " desc limit 1"
                )
            }
            cursor?.takeIf { it.moveToFirst() }?.use {
                //媒体id,兼容不同手机返回的uri可能不带媒体文件id,需要手动获取,然后拼接
                val id = it.getColumnIndex(ImageColumns._ID).takeIf { it >= 0 }?.let(it::getLong) ?: 0L
                //媒体路径
                val path = it.getColumnIndex(ImageColumns.DATA).takeIf { it >= 0 }?.let(it::getString) ?: ""
                //当前文件对应用是否可见  0:可见
                val isPending = if (VERSION.SDK_INT >= VERSION_CODES.Q) {
                    cursor.getColumnIndex(ImageColumns.IS_PENDING).takeIf { it >= 0 }?.let(cursor::getInt) ?: 0
                } else 0

                //处理获取到的数据
                handleMediaData(activity, checkUri(uri, id), path, isPending == 0, uid, hostId)
            } ?: run {
                cursor?.close()
                Log.d(TAG, "查询数据为空,检查不同机型权限")
            }
        }.onFailure {
            Log.d(TAG, "查询数据失败=${it.message}")
        }
    }

    /**
     * 处理不同机型 返回uri兼容性问题:
     *     返回的uri如果没有id,手动添加
     *     content://media/external/images/media
     *     content://media/external/images/media/7667
     */
    private fun checkUri(uri: Uri, id: Long): Uri = try {
        ContentUris.parseId(uri)
        uri
    } catch (e: NumberFormatException) {
        ContentUris.withAppendedId(uri, id)
    }

    /**
     * 处理获取到的数据
     * @param isPending 当前文件对应用是否可见,可见状态应用才能处理文件
     */
    private fun handleMediaData(activity: Activity, uri: Uri, path: String, isPending: Boolean, uid: String, hostId: String) {
        Log.d(TAG, "查询到系统文件变更,isPending=$isPending path=$path")

        //文件路径是否包含 截屏/录屏 特征的关键字:不包含则认为当前没有截屏/录屏
        //如果文件有带截图关键字,但又带有target,也不处理此文件:这是处理过重命名的文件
        if (path.isBlank()) return
        val newPath = path.lowercase(Locale.getDefault())
        if (!newPath.contains("screen")
            && !newPath.contains("截屏")
            && !newPath.contains("录屏")
            || newPath.contains(".trashed-")//Android12 垃圾文件
            || newPath.contains("_target")//App已处理过并生成的目标文件,不在处理
        ) {
            return
        }

        //多次回调只通知一次
        if (checkCallbackPath(path)) return

        //填充水印数据,填充完需要延迟一下,防止view的bitmap获取为null
        createTextView(activity, uid, hostId) {
            //如需删除源文件,Android11+外部需申请权限:  MediaStore.createWriteRequest(contentResolver,uri)
            completeResult?.invoke(ScreenResBean(uri, path, isPending, watermarkView))
        }
    }

    /**
     * 创建填充水印
     */
    private fun createTextView(activity: Activity, uid: String, hostId: String, callback: () -> Unit) {
        val sb = StringBuilder("UID:$uid\n")
        if (!hostId.isEmpty()) sb.append("HostID:$hostId\n")
        sb.append(SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().time))
        watermarkView = TextView(activity).apply {
            text = sb.toString()
            textSize = 15f
            setTextColor(Color.parseColor("#80ff0000"))
            typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            gravity = Gravity.END or Gravity.BOTTOM
            setPadding(0, 0, 380, 500)
            setShadowLayer(2f, 2f, 2f, Color.parseColor("#66000000"))
            isInvisible = true
            if (parent == null) activity.findViewById<FrameLayout>(android.R.id.content)?.addView(this)
        }
        //延迟一下,防止后续立刻获取view的bitmap为null
        CoroutineScope(SupervisorJob()).launch {
            delay(50)
            callback.invoke()
        }
    }

    /**
     * 判断当前文件是否已回调过,防止某些手机一次发出多次内容改变的通知
     */
    private fun checkCallbackPath(imagePath: String): Boolean {
        if (callbackPaths.contains(imagePath)) {
            Log.d(TAG, "$imagePath 文件已处理,不再重复操作")
            return true
        }
        //记录 8 条数据
        callbackPaths.add(imagePath)
        if (callbackPaths.size > 8) callbackPaths.removeAt(0)
        return false
    }

    /**
     * 媒体内容观察者
     */
    private inner class MediaContentObserver(handler: Handler?) : ContentObserver(handler) {
        /**
         * 通过媒体数据库的内容改变来判断用户是否执行了截屏/录屏操作
         * 应用处于后台不处理
         */
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            super.onChange(selfChange, uri)
            Log.d(TAG, "isFront=${ActivityStackManager.isFront}  ContentObserver onChange uri=$uri")
            //当前页面是否处于前台,前台才监听媒体变更
            if (ActivityStackManager.isFront.not() || null == uri) return
            //外部申请存储权限
            storageApplyResult?.invoke(uri)
        }
    }

    companion object {
        private const val TAG = "ScreenShotManager"

        @JvmStatic
        val instance by lazy(NONE) { ScreenShotManager() }
    }
}