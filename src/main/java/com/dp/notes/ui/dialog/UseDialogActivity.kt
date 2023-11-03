package com.dp.notes.ui.dialog

import android.content.Intent
import android.os.Bundle
import com.dp.core.base.BaseActivity
import com.dp.core.extension.clickEvent
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesActivityUseDialogBinding


/**
 * author Dq
 * date on 2023/1/11
 * description Dialog使用
 */
class UseDialogActivity : BaseActivity(R.layout.notes_activity_use_dialog) {
    private val binding by bindings<NotesActivityUseDialogBinding>()

    private val hideDialog by lazy { ChildFragmentDialog() }

    override fun initView(bundle: Bundle?) {

    }

    override fun initListener() {
        binding.bt1.clickEvent {
            //TestDialog(this).show()
            ParentFragmentDialog.show(this)
        }

        binding.bt2.clickEvent {
            hideDialog.show(this)
            //ChildFragmentDialog.show(this)
            //DefaultDialog.show(supportFragmentManager)
        }

        binding.bt3.clickEvent {
            startActivity(Intent(this, DialogActivity::class.java))
            //navigateTo<DialogActivity>()
        }

        binding.bt4.clickEvent {
            startActivity(Intent(this, DialogBottomActivity::class.java))
            //navigateTo<DialogBottomActivity>()
        }
    }
}