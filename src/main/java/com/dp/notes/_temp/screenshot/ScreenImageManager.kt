@file:Suppress("DEPRECATION")

package com.dp.notes._temp.screenshot

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.provider.MediaStore
import android.util.Log
import com.dp.core.CoreManager
import kotlinx.coroutines.*
import java.io.File
import kotlin.LazyThreadSafetyMode.NONE


/**
 * author Dq
 * date on 2023/2/18
 * description 系统截屏 图片处理
 */
class ScreenImageManager private constructor() {

    /**
     * 系统截图 添加水印
     * @param bean 截图数据
     */
    fun handleImage(bean: ScreenResBean) {
        //图片文件不对外,不处理
        if (bean.isPending.not()) return

        //获取水印View bitmap
        bean.tagView?.isDrawingCacheEnabled = true
        bean.tagView?.buildDrawingCache()
        val tagBitmap = bean.tagView?.drawingCache
        Log.d(TAG, "水印View生成 Bitmap=$tagBitmap")
        if (null == tagBitmap) return

        //获取View在屏幕上的坐标
        val location = IntArray(2)
        bean.tagView.getLocationInWindow(location)

        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            runCatching {
                //截图转bitmap
                val bitmap = BitmapFactory.decodeFile(bean.path)
                //截图和水印合成一张图片bitmap,保存覆盖原截图
                val newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                val canvas = Canvas(newBitmap)
                canvas.drawBitmap(tagBitmap, location[0].toFloat(), location[1].toFloat(), null)
                //保存覆盖相册原有图片
                saveImageToPhoto(bean, newBitmap)
                //销毁
                bean.tagView.destroyDrawingCache()
            }.onFailure {
                Log.d(TAG, "截图添加水印处理失败=${it.message}")
            }
            cancel()
        }
    }

    /**
     * 添加水印后的截图 覆盖原有截图到系统相册
     * Android11+ 需要用户同意权限
     * 外部已经 try catch,这里不需要
     */
    private fun saveImageToPhoto(bean: ScreenResBean, bitmap: Bitmap) {
        if (VERSION.SDK_INT >= VERSION_CODES.R) {
            //Android11+ 截图复制到系统相册并删除原文件
            CoreManager.app.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues().apply {
                put(MediaStore.Images.Media.RELATIVE_PATH, bean.targetRelativePath)
                //文件名带上 _target标识 : 用于过滤此文件
                put(MediaStore.Images.Media.DISPLAY_NAME, bean.targetName + ".jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/*")
            })?.let {
                //拷贝 图片文件 到相册的uri
                CoreManager.app.contentResolver.openOutputStream(it)?.use {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)
                    //Android11+ 删除文件需要额外权限
                    val deleteResult = File(bean.path).delete()
                    Log.d(TAG, "Android 11+:添加水印后的截图已保存至相册,源文件删除结果=$deleteResult")
                }
            }
        } else {
            //Android10- 添加水印后的截图 覆盖系统相册源文件
            CoreManager.app.contentResolver.openOutputStream(bean.uri)?.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)
                Log.d(TAG, "Android10-:添加水印后的截图已覆盖保存到相册")
            }
        }
    }

    companion object {
        private const val TAG = "ScreenImageManager"

        @JvmStatic
        val instance by lazy(NONE) { ScreenImageManager() }
    }
}