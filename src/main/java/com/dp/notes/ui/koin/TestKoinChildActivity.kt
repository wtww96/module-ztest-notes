package com.dp.notes.ui.koin

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.dp.core.base.BaseActivity
import com.dp.core.extension.clickEvent
import com.dp.core.network.failure
import com.dp.core.network.launchIn
import com.dp.core.network.wrap
import com.dp.core.network.success
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesActivityTestBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinScopeComponent
import org.koin.core.scope.Scope

/**
 * author Dq
 * date on 2022/9/14
 * description
 */
class TestKoinChildActivity : BaseActivity(R.layout.notes_activity_test), KoinScopeComponent {
    private val binding by bindings<NotesActivityTestBinding>()
    private val viewModel by viewModel<TestKoinViewModel>()

    override val scope: Scope by lazy { getKoin().getScopeOrNull("123")!! }

    override fun initView(bundle: Bundle?) {
        Log.e("hehe", "TestKoinChildActivity >>>>>>>>>>>>>>>>> viewModel = $viewModel")
        Log.e("hehe", "TestKoinChildActivity >>>>>>>>>>>>>>>>> scope = $scope")
        binding.title.text = "Koin 依赖注入 子页面>>>>>>>>>>>"
    }

    override fun initObserve() {
        viewModel.requestNew().wrap(this) {
            success {
                binding.textview.add("flowRequest接收数据=${it.text}")
            }
            failure { e, data ->
                binding.textview.add("flowRequest接收数据,失败=${e.message}")
            }
        }
        //更新LiveData
        viewModel.result.observe(this) {
            binding.textview.add("requestRx,通过LiveData更新数据=$it")
        }

        //更新StateFlow
        viewModel.resultState.launchIn(this) {
            binding.textview.add("更新StateFlow=$it")
        }
        //更新SharedFlow
        viewModel.resultShared.launchIn(this) {
            binding.textview.add("更新SharedFlow=$it")
        }
    }

    override fun initListener() {
        binding.button.clickEvent {
            binding.textview.add("开始请求rxRequest-->")
            viewModel.getRequest()
        }
        binding.button1.clickEvent {
            lifecycleScope.launch {
                delay(2000)
                viewModel.changeState("延迟2s发送的数据")
                delay(2000)
                viewModel.changeState("延迟4s发送的数据")
            }
        }
    }
}

