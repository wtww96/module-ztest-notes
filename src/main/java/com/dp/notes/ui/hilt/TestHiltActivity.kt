package com.dp.notes.ui.hilt

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.dp.core.base.BaseActivity
import com.dp.core.extension.clickEvent
import com.dp.core.network.failure
import com.dp.core.network.launchWith
import com.dp.core.network.success
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesActivityTestBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * author Dq
 * date on 2022/9/14
 * description
 */
@AndroidEntryPoint
class TestHiltActivity : BaseActivity(R.layout.notes_activity_test) {
    private val binding by bindings<NotesActivityTestBinding>()
    private val viewModel by viewModels<TestHiltViewModel>()

    override fun initView(bundle: Bundle?) {
        binding.title.text = "Hilt 依赖注入"
        binding.button1.isVisible = false
    }

    override fun initObserve() {
        //回调接口转Api,通过LiveData更新数据
        viewModel.rxResult.observe(this) {
            binding.textview.add("rxResult接收数据=$it")
        }

        //flow流数据,直接返回给Activity,不需要再定义LiveData
        viewModel.flowRequest2().launchWith(this) {
            success {
                binding.textview.add("flowRequest接收数据=${it.text}")
            }
            failure { e, data ->
                binding.textview.add("flowRequest接收数据,失败=${e.message}")
            }
        }
    }

    override fun initListener() {
        binding.button.clickEvent {
            binding.textview.add("开始请求rxRequest-->")
            viewModel.rxRequest()
        }
    }
}