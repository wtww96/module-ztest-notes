package com.dp.notes.ui.dialog

import android.view.Gravity
import com.dp.core.base.BaseFragmentDialog
import com.dp.core.extension.clickEvent
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesDialogTestBinding

/**
 * author Dq
 * date on 2022/10/19
 * description
 */
class ChildFragmentDialog : BaseFragmentDialog(R.layout.notes_dialog_test) {
    private val binding by bindings<NotesDialogTestBinding>()

    override fun initView() {
        binding.button.text = "加上健康登记卡数据库"
        binding.button.clickEvent { dismiss() }
    }

    override fun dialogGravity(): Int = Gravity.BOTTOM

    companion object {
        fun show(any: Any) {
            ChildFragmentDialog().show(any)
        }
    }
}