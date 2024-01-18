package com.dp.notes.ui.dialog

import android.util.Log
import android.view.WindowManager
import com.dp.core.base.BaseFragmentDialog
import com.dp.core.extension.clickEvent
import com.dp.core.extension.dp
import com.dp.core.viewbinding.bindings
import com.dp.core.windowinsets.*
import com.dp.notes.R
import com.dp.notes.databinding.NotesDialogTestBinding

/**
 * author Dq
 * date on 2022/10/19
 * description
 */
class ParentFragmentDialog : BaseFragmentDialog(R.layout.notes_dialog_test) {
    private val binding by bindings<NotesDialogTestBinding>()

    override fun initView() {
        //弹窗弹出时,点击事件可以穿透到下面的View
        dialog?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)

        binding.button.clickEvent {
            ChildFragmentDialog.show(this)
            dismiss()
        }

        binding.button1.clickEvent {
            Log.e("hehe", "DialogFragment ime isVisible = ${imeVisible()}")
        }

        ImeHelper.addImeChangeCallback(this) {
            onStatus {

            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.e("hehe", ">>>>>>>>>>> onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e("hehe", ">>>>>>>>>>> onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.e("hehe", ">>>>>>>>>>> onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e("hehe", ">>>>>>>>>>> onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("hehe", ">>>>>>>>>>> onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("hehe", ">>>>>>>>>>> onDestroy")
    }

    override fun dialogHMargin(): Int = 20.dp

    companion object {
        fun show(any: Any) {
            ParentFragmentDialog().show(any)
        }
    }
}