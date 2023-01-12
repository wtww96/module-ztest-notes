package com.dp.notes.ui.dialog

import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesDialogMainBinding

/**
 * author Dq
 * date on 2023/1/11
 * description Activity 弹窗
 */
class DialogActivity : BaseTransparentActivity(R.layout.notes_dialog_main) {
    private val binding by bindings<NotesDialogMainBinding>()

    override fun initView() {
    }

    override fun initListener() {
    }

    override fun dimAmount(): Float = 0.4f
}