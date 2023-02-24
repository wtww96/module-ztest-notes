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
import io.microshow.rxffmpeg.RxFFmpegInvoke
import io.microshow.rxffmpeg.RxFFmpegSubscriber
import kotlinx.coroutines.*
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import kotlin.LazyThreadSafetyMode.NONE


/**
 * author Dq
 * date on 2023/2/21
 * description 系统录屏 ffmpeg给视频开头添加静态图片
 */
class ScreenVideoManager private constructor() {
    private lateinit var resBean: ScreenResBean

    //FFmpeg文件
    private lateinit var ffmpegDir: File
    private var externalPath = ""

    //录屏视频源文件
    private var sourcePath = ""

    //输出的视频.ts文件路径
    private var outVideoTsPath = ""

    //图片转视频的地址,不带后缀
    private var imageVideoPath = ""

    private var time = 0L

    /**
     * 给目标视频添加 1s中的静态图片
     */
    fun handleVideo(bean: ScreenResBean) {
        //视频文件不对外,不处理
        if (bean.isPending.not()) return

        resBean = bean
        time = System.currentTimeMillis()
        //=================================================
        ffmpegDir = File(CoreManager.app.externalCacheDir, "ffmpeg")
        if (ffmpegDir.exists().not()) ffmpegDir.mkdirs() else deleteffDir()
        externalPath = ffmpegDir.toString() + File.separator
        //=================================================
        sourcePath = bean.path
        outVideoTsPath = externalPath + "outVideo_ts.ts"
        imageVideoPath = externalPath + "imageVideo"

        //截取视频第一帧图,添加水印,合成到视频开头
        getFirstFrameAddTag {
            pictureToMp4(it)
        }
    }

    /**
     * 获取视频第一帧,添加水印,缓存到本地
     */
    private fun getFirstFrameAddTag(callback: (String) -> Unit) {
        val watermarkImagePath = externalPath + "watermark.jpg"//最终合成的带水印的图
        val firstFrameImagePath = externalPath + "firstFrame.jpg"//视频第一帧图
        val commands = "ffmpeg -y -i $sourcePath -f image2 -ss 1 -frames:v 1 $firstFrameImagePath".split(" ").toTypedArray()

        RxFFmpegInvoke.getInstance()
            .runCommandRxJava(commands)
            .subscribe(VideoRxFFmpegSubscriber {
                Log.d(TAG, "ffmpeg----------> 获取视频第一帧图片成功")
                //水印bitmap
                resBean.tagView?.isDrawingCacheEnabled = true
                resBean.tagView?.buildDrawingCache()
                resBean.tagView?.drawingCache?.let {
                    //第一帧图片转bitmap
                    val bitmap = BitmapFactory.decodeFile(firstFrameImagePath)

                    //水印在屏幕上的坐标
                    val location = IntArray(2)
                    resBean.tagView?.getLocationInWindow(location)

                    //截图和水印合成一张图片bitmap
                    val newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                    val canvas = Canvas(newBitmap)
                    canvas.drawBitmap(it, location[0].toFloat(), location[1].toFloat(), null)

                    //缓存图片到本地
                    BufferedOutputStream(FileOutputStream(File(watermarkImagePath))).use {
                        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, it)
                        it.flush()
                    }

                    //销毁
                    resBean.tagView?.destroyDrawingCache()
                    bitmap.recycle()

                    callback.invoke(watermarkImagePath)
                }
            })
    }

    /**
     * 1.静态图片转视频.mp4
     */
    private fun pictureToMp4(imagePath: String) {
        val width = resBean.screenSize.x
        val height = resBean.screenSize.y
        val text =
            "ffmpeg -y -f image2 -loop 1 -i $imagePath -s ${width}x${height} -t 00:00:01 -pix_fmt yuvj420p -vb 3000k -preset ultrafast $imageVideoPath.mp4"
        val commands = text.split(" ").toTypedArray()
        //开始执行FFmpeg命令
        RxFFmpegInvoke.getInstance()
            .runCommandRxJava(commands)
            .subscribe(VideoRxFFmpegSubscriber {
                Log.d(TAG, "ffmpeg----------> 静态图片转视频.mp4 成功")
                pictureMp4ToTs()
            })
    }

    /**
     * 2.静态图片视频.mp4 转 .ts
     */
    private fun pictureMp4ToTs() {
        val text = "ffmpeg -y -i $imageVideoPath.mp4 -vcodec copy -acodec copy -vbsf h264_mp4toannexb $imageVideoPath.ts"
        val commands = text.split(" ").toTypedArray()

        //开始执行FFmpeg命令
        RxFFmpegInvoke.getInstance()
            .runCommandRxJava(commands)
            .subscribe(VideoRxFFmpegSubscriber {
                Log.d(TAG, "ffmpeg----------> 静态图片视频.mp4 转 .ts 成功")
                targetMp4ToTs()
            })
    }

    /**
     * 3.源视频.mp4 转 .ts
     */
    private fun targetMp4ToTs() {
        val text = "ffmpeg -y -i $sourcePath -vcodec copy -acodec copy -vbsf h264_mp4toannexb $outVideoTsPath"
        val commands = text.split(" ").toTypedArray()

        //开始执行FFmpeg命令
        RxFFmpegInvoke.getInstance()
            .runCommandRxJava(commands)
            .subscribe(VideoRxFFmpegSubscriber {
                Log.d(TAG, "ffmpeg----------> 目标视频.mp4 转 .ts 成功")
                concatTsToMp4()
            })
    }


    /**
     * 4.合并.ts的视频文件 转 .mp4
     */
    private fun concatTsToMp4() {
        //视频合并输出的文件
        val outVideoPath = externalPath + "outVideo_${System.currentTimeMillis()}.mp4"
        val text = "ffmpeg -y -i concat:$imageVideoPath.ts|$outVideoTsPath -acodec copy -vcodec copy $outVideoPath"
        val commands = text.split(" ").toTypedArray()

        //开始执行FFmpeg命令
        RxFFmpegInvoke.getInstance()
            .runCommandRxJava(commands)
            .subscribe(VideoRxFFmpegSubscriber {
                Log.d(TAG, "ffmpeg----------> 合并.ts的视频文件 转 .mp4 成功,耗时=${(System.currentTimeMillis() - time)}")
                copyVideoToPhoto(outVideoPath)
            })
    }

    /**
     * 将最终输出的合成视频文件 复制到系统相册,并删除原文件
     */
    private fun copyVideoToPhoto(outVideoPath: String) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            runCatching {
                //合成后的视频文件
                val outVideoFile = File(outVideoPath)
                if (VERSION.SDK_INT >= VERSION_CODES.Q) {
                    //Android 10+
                    CoreManager.app.contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, ContentValues().apply {
                        put(MediaStore.Video.Media.RELATIVE_PATH, resBean.targetRelativePath)
                        //文件名带上 _target标识 : 用于过滤此文件
                        put(MediaStore.Video.Media.DISPLAY_NAME, resBean.targetName)
                        put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
                    })?.let {
                        //拷贝 视频文件 到相册的uri
                        CoreManager.app.contentResolver.openOutputStream(it)?.use {
                            Files.copy(outVideoFile.toPath(), it)
                            deleteSource()
                        }
                    }
                } else {
                    //Android9-
                    FileInputStream(outVideoFile).use { ins ->
                        val temp = ByteArray(1024)
                        var len: Int
                        //写入到相册
                        FileOutputStream(File(resBean.targetVideoPath)).buffered().use { ous ->
                            while (ins.read(temp).also { len = it } > 0) ous.write(temp, 0, len)
                        }
                        deleteSource()
                    }
                }
            }.onFailure {
                Log.d(TAG, "视频复制到相册,删除原文件 操作失败=${it.message}")
            }
            cancel()
        }
    }

    /**
     * 删除相册源文件
     * 注意-------->
     *    如果手机开启了相册云同步功能,删除本地相册文件成功,可能后面还会从云端恢复出来
     */
    private fun deleteSource() {
        //删除相册源文件
        val deleteResult = CoreManager.app.contentResolver.delete(resBean.uri, null, null)
        //删除ffmpeg缓存的文件
        deleteffDir()
        Log.d(TAG, "合成的视频文件已移动到相册,源文件删除结果=${deleteResult == 1}")
    }

    private fun deleteffDir() {
        ffmpegDir.listFiles()?.forEach { it.delete() }
    }

    inner class VideoRxFFmpegSubscriber(val finish: () -> Unit) : RxFFmpegSubscriber() {
        override fun onFinish() {
            finish.invoke()
        }

        override fun onProgress(progress: Int, progressTime: Long) {
            Log.d(TAG, "ffmpeg----------> onProgress =$progress")
        }

        override fun onCancel() {
            Log.d(TAG, "ffmpeg----------> onCancel")
            deleteffDir()
        }

        override fun onError(message: String?) {
            Log.d(TAG, "ffmpeg----------> onError =$message")
            deleteffDir()
        }
    }

    companion object {
        private const val TAG = "ScreenVideoManager"

        @JvmStatic
        val instance by lazy(NONE) { ScreenVideoManager() }
    }
}