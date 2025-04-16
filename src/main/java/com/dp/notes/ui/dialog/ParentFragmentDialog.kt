package com.dp.notes.ui.dialog

import android.util.Log
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.dp.core.base.BaseFragmentDialog
import com.dp.core.extension.clickEvent
import com.dp.core.extension.dp
import com.dp.core.windowinsets.ImeHelper
import com.dp.core.windowinsets.imeVisible
import com.dp.notes.databinding.NotesDialogTestBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * author Dq
 * date on 2022/10/19
 * description
 */
class ParentFragmentDialog : BaseFragmentDialog<NotesDialogTestBinding>() {

    override fun register() = binding.text
    override fun initView() {
        //window窗口区域以外的点击事件传递给下层window,区域以内的点击事件自己处理
        dialog?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)

        lifecycleScope.launch {
            delay(2000)
            loadSir.showSuccess()
        }

        binding.button.clickEvent {
            ChildFragmentDialog.show(this)
            //dismiss()
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
        Log.e("hehe", ">>>>>>>>>>> ParentFragmentDialog onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e("hehe", ">>>>>>>>>>> ParentFragmentDialog onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.e("hehe", ">>>>>>>>>>> ParentFragmentDialog onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e("hehe", ">>>>>>>>>>> ParentFragmentDialog onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("hehe", ">>>>>>>>>>> ParentFragmentDialog onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("hehe", ">>>>>>>>>>> ParentFragmentDialog onDestroy")
    }

    override fun dialogHMargin(): Int = 20.dp

    companion object {
        fun show(any: Any) {
            ParentFragmentDialog().show(any)
        }
    }
}