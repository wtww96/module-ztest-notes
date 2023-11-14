package com.dp.notes.ui.dialog

import android.util.Log
import android.view.Gravity
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

    override fun onStart() {
        super.onStart()
        Log.e("hehe", " ------------- onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e("hehe", " ------------- onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.e("hehe", " ------------- onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e("hehe", " ------------- onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("hehe", " ------------- onDestroy")
    }

    override fun dialogGravity(): Int = Gravity.BOTTOM
    override fun useHide(): Boolean = true

    companion object {
        fun show(any: Any) {
            HideDialog().show(any)
        }
    }
}