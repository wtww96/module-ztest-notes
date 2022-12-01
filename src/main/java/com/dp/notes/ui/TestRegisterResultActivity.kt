package com.dp.notes.ui

import android.content.Intent
import android.view.View
import com.dp.core.base.BaseActivity
import com.dp.core.extension.clickEvent
import com.dp.core.viewbinding.bindings
import com.dp.notes.databinding.ActivityTestRegisterResultBinding

/**
 * author Dq
 * date on 2022/11/11
 * description registerResult的使用
 */
class TestRegisterResultActivity : BaseActivity() {
    private val binding by bindings<ActivityTestRegisterResultBinding>()

    override fun getLayoutView(): View = binding.root

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