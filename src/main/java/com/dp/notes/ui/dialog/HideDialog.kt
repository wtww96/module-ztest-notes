package com.dp.notes.ui.dialog

import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import com.dp.core.base.BaseFragmentDialogHide
import com.dp.core.extension.clickEvent
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesDialogTestBinding

/**
 * author Dq
 * date on 2023/11/7
 * description
 */
class HideDialog : BaseFragmentDialogHide(R.layout.notes_dialog_test) {
    private val binding by bindings<NotesDialogTestBinding>()

    override fun initView() {
        Log.e("hehe", " ------------- initView")
        //弹窗弹出时,点击事件可以穿透到下面的View
        dialog?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
    }

    override fun initListener() {
        binding.button1.clickEvent {
            dismiss()
        }
        binding.button.clickEvent {
            hide()
        }
        dialog?.setOnDismissListener { }
    }

    override fun dialogGravity(): Int = Gravity.BOTTOM
    override fun useHide(): Boolean = true

    companion object {
        fun show(any: Any) {
            HideDialog().show(any)
        }
    }
}