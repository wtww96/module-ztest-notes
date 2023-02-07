package com.dp.notes.ui.dialog

import android.os.Bundle
import com.dp.core.base.BaseTransparentActivity
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesDialogBottomBinding

/**
 * author Dq
 * date on 2023/1/11
 * description Activity 底部弹窗
 */
class DialogBottomActivity : BaseTransparentActivity(R.layout.notes_dialog_bottom) {
    private val binding by bindings<NotesDialogBottomBinding>()

    override fun initView(bundle: Bundle?) {
    }

    override fun initListener() {
    }
}