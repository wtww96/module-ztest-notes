package com.dp.notes.dialog


import android.util.Log
import com.dp.core.base.BaseFragmentDialog
import com.dp.core.extension.clickEvent
import com.dp.core.extension.dp
import com.dp.core.viewbinding.bindings
import com.dp.core.windowinsets.*
import com.dp.notes.R
import com.dp.notes.databinding.DialogTest1Binding

/**
 * author Dq
 * date on 2022/10/19
 * description
 */
class ParentDialog : BaseFragmentDialog(R.layout.dialog_test1) {
    private val binding by bindings<DialogTest1Binding>()

    override fun initView() {
        binding.button.clickEvent {
            ChildDialog.show(this)
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

    override fun dialogHMargin(): Int = 20.dp

    companion object {
        fun show(any: Any) {
            ParentDialog().show(any)
        }
    }
}