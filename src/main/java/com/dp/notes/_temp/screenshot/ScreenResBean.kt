package com.dp.notes._temp.screenshot

import android.content.Context
import android.graphics.Point
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Environment
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.dp.core.CoreManager
import java.io.File

/**
 * author Dq
 * date on 2023/2/18
 * description 系统 截屏/录屏 回调数据
 */
data class ScreenResBean(
    //文件Uri
    val uri: Uri,
    //文件路径
    val path: String,
    //Android10+ 文件对应用是否可见,可见:App才能处理文件
    val isPending: Boolean,
    //水印View
    val tagView: View?
) {

    /**
     * 是否是图片
     */
    val isImage: Boolean
        get() = path.endsWith(".mp4", true).not()

    /**
     * 是否是 Android11+开启了分区存储,开启了的话操作文件用uri
     * Android11+ :强制开启
     * Android10及以下 :关闭
     */
    val isScopedStorage: Boolean
        get() = VERSION.SDK_INT >= VERSION_CODES.R

    /**
     * 手机屏幕真实宽高
     */
    val screenSize: Point
        get() {
            val windowManager = CoreManager.app.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val point = Point()
            if (VERSION.SDK_INT >= VERSION_CODES.R) {
                point.x = windowManager.currentWindowMetrics.bounds.width()
                point.y = windowManager.currentWindowMetrics.bounds.height()
            } else {
                @Suppress("DEPRECATION")
                windowManager.defaultDisplay.getRealSize(point)
            }
            return point
        }

    /**
     * Android10+ 输出的目标文件 的相对路径 : DCIM/Screenshots
     */
    val targetRelativePath: String
        @RequiresApi(VERSION_CODES.Q)
        get() = Environment.DIRECTORY_DCIM + File.separator + (File(path).parentFile?.name ?: Environment.DIRECTORY_SCREENSHOTS)

    /**
     * Android10+ 输出的目标文件 的文件名称,加上 _target 标识
     */
    val targetName: String
        @RequiresApi(VERSION_CODES.Q)
        get() = File(path).name.lastIndexOf(".").takeIf { it > -1 }?.let {
            File(path).name.substring(0, it).plus("_target")
        } ?: run { System.currentTimeMillis().toString().plus("_target") }

    /**
     * Android10- 输出的视频文件 的 绝对路径
     */
    val targetVideoPath: String
        get() = path.lastIndexOf(".").takeIf { it > -1 }?.let {
            StringBuilder(path).insert(it, "_target").toString()
        } ?: run { path.plus("_target.mp4") }
}
