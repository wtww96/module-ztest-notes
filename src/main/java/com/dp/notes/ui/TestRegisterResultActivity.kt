package com.dp.notes.ui

import android.content.Intent
import com.dp.core.base.BaseActivity
import com.dp.core.extension.clickEvent
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesActivityTestRegisterResultBinding

/**
 * author Dq
 * date on 2022/11/11
 * description registerResult的使用
 */
class TestRegisterResultActivity : BaseActivity(R.layout.notes_activity_test_register_result) {
    private val binding by bindings<NotesActivityTestRegisterResultBinding>()

    override fun initListener() {
        binding.button1.clickEvent {
            finish()
        }
        binding.button2.clickEvent {
            //setResult(RESULT_OK)
            setResult(666, Intent().putExtra("backParams", "发送的:finish回调的参数"))
            finish()
        }
    }
}