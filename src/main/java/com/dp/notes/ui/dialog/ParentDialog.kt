package com.dp.notes.ui.dialog


import android.util.Log
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
class ParentDialog : BaseFragmentDialog(R.layout.notes_dialog_test) {
    private val binding by bindings<NotesDialogTestBinding>()

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