package com.dp.notes.ui.wx.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager.LayoutParams
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.dp.notes.R
import com.dp.notes.databinding.NotesDialogRecordBinding

/**
 * author Dq
 * date on 2025/7/9
 * description 录音弹窗
 */
class RecordDialog : DialogFragment(R.layout.notes_dialog_record) {
    private var _binding: NotesDialogRecordBinding? = null
    private val binding: NotesDialogRecordBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, com.dp.core.R.style.Theme_Dialog_Base)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.window?.let {
            it.setWindowAnimations(0)
            val layoutParams = it.attributes
            layoutParams.height = LayoutParams.MATCH_PARENT
            layoutParams.width = LayoutParams.MATCH_PARENT
            it.setAttributes(layoutParams)
        }
        _binding = NotesDialogRecordBinding.bind(view)
        initView()
    }

    private fun initView() {
        // 默认发送状态
        sendStatus()
    }

    private fun sendStatus() {
        binding.ivDelete.setImageResource(R.drawable.notes_ic_cancle_record_no)
        binding.tvStatus.text = "松开 发送"
    }

    private fun cancleStatus() {
        binding.ivDelete.setImageResource(R.drawable.notes_ic_cancle_record_yes)
        binding.tvStatus.text = "松手 取消"
    }

    /**
     * 外部更新UI状态
     */
    fun updateUIStatus(isCanceled: Boolean) {
        if (_binding == null) return
        if (!isCanceled) {
            sendStatus()
        } else {
            cancleStatus()
        }
    }

    /**
     * 更新音量波纹动画
     */
    fun updateWave(voiceSize: Int) {
        binding.waveView1.addVoiceSize(voiceSize)
        binding.waveView2.addVoiceSize(voiceSize)
    }

    /**
     * 底部触摸区域Y: 用于判断手指是否滑出这个区域
     */
    var bottomTouchAreaY: Int = 0
        private set
        get() {
            if (field <= 0) {
                val location = IntArray(2)
                _binding?.ivBg?.getLocationOnScreen(location)
                field = location[1]
            }
            return field
        }

    override fun dismiss() {
        // super.dismiss()
        if (isAdded) dismissAllowingStateLoss()
        binding.waveView2.quit()
    }

    fun show(any: Any?) {
        val fm = when (any) {
            is FragmentActivity -> any.supportFragmentManager
            is DialogFragment -> any.parentFragmentManager
            is Fragment -> any.childFragmentManager
            is FragmentManager -> any
            else -> null
        }
        if (any is FragmentActivity && (any.isFinishing || any.isDestroyed)) return

        if (!isAdded && fm?.isDestroyed == false && !fm.isStateSaved) {
            super.show(fm, javaClass.canonicalName)
        }
    }

    companion object {
        fun show(context: Context) {
            RecordDialog().show(context)
        }
    }
}