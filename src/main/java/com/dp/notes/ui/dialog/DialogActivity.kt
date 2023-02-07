package com.dp.notes.ui.dialog

import android.os.Bundle
import com.dp.core.base.BaseTransparentActivity
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesDialogMainBinding

/**
 * author Dq
 * date on 2023/1/11
 * description Activity 居中弹窗
 */
class DialogActivity : BaseTransparentActivity(R.layout.notes_dialog_main) {
    private val binding by bindings<NotesDialogMainBinding>()

    override fun initView(bundle: Bundle?) {
    }

    override fun initListener() {
    }

    override val dimAmount: Float get() = 0.5f
}