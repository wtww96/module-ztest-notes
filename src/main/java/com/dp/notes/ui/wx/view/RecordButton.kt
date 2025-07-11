package com.dp.notes.ui.wx.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView
import com.dp.notes.ui.wx.RecorderUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.min

/**
 * author Dq
 * date on 2025/7/9
 * description 语音录制按钮
 */
class RecordButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {
    // 录音工具类
    private val recorder by lazy { RecorderUtil() }

    // 录音弹窗
    private var recordDialog: RecordDialog? = null

    init {
        text = "按住 说话"
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                // 按下:显示录音弹窗并开始录音
                text = "松开 发送"
                initDialogAndStartRecord()
            }

            MotionEvent.ACTION_MOVE -> {
                // 滑动: 更新UI状态(取消/结束)
                updateRecordUIStatus(event)
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                // 抬起: 取消录音 / 结束录音
                text = "按住录音"
                if (isRecordingCanceled(event)) cancelRecord() else finishRecord()
            }
        }

        return true
    }

    /**
     * 初始化录音弹窗 / 开始录音
     */
    private fun initDialogAndStartRecord() {
        recordDialog = recordDialog ?: RecordDialog()

        if (startRecording()) {
            recordDialog?.show(context)
        }
    }

    /**
     * 手指滑动: 更新录音UI状态
     * 判断是否移到发送区域外,取消录音
     */
    private fun updateRecordUIStatus(event: MotionEvent) {
        val isCanceled = isRecordingCanceled(event)
        recordDialog?.updateUIStatus(isCanceled)
    }

    private var amplitudeJob: Job? = null

    /**
     * 开始录音
     */
    private fun startRecording(): Boolean {
        // 开始录音
        val isSuccess = recorder.startRecording()
        if (!isSuccess) return false
        // 录音成功,定时获取录音音量
        amplitudeJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                delay(500)
                val amplitude = recorder.maxAmplitude
                val voiceSize = ((amplitude / 32767f) * 100).toInt().coerceIn(0, 100)

                val db = (amplitude / 35).coerceAtMost(200)
                //Log.e("hehe", "amplitude=$amplitude  ,voiceSize=$voiceSize , db=$db")
                recordDialog?.updateWave(db)
            }
        }

        return true
    }

    /**
     * 取消录音
     */
    private fun cancelRecord() {
        recordDialog?.dismiss()
        releaseRecord()
    }

    /**
     * 结束录音
     */
    private fun finishRecord() {
        recordDialog?.dismiss()
        releaseRecord()
    }

    private fun releaseRecord() {
        amplitudeJob?.cancel()
        amplitudeJob = null
        recorder.stopRecording()
    }

    /**
     * 录音是否是取消的UI状态
     */
    private fun isRecordingCanceled(event: MotionEvent): Boolean {
        val dialog = recordDialog ?: return false
        if (dialog.dialog?.isShowing != true) return false
        // 手指滑动的rawY < 底部触摸区域所在页面的Y位置
        return event.rawY < dialog.bottomTouchAreaY
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        amplitudeJob?.cancel()
        amplitudeJob = null
    }
}