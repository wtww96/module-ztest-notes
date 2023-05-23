package com.dp.notes.ui.state

import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.dp.common.widget.state.LoadStateView
import com.dp.common.widget.state.StateView
import com.dp.core.base.BaseFragment
import com.dp.core.extension.clickEvent
import com.dp.core.extension.delayed
import com.dp.core.extension.showToast
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesFragmentChildBinding
import kotlinx.coroutines.delay

/**
 * author Dq
 * date on 2023/5/21
 * description
 */
class StateFragment1 : BaseFragment(R.layout.notes_fragment_child) {
    private val binding by bindings<NotesFragmentChildBinding>()

    private var type = ""

    private val state by lazy { LoadStateView.inject(this) }

    override fun initView() {
        arguments?.let { type = it.getString("type", "") }
        binding.textview.clear()
        binding.textview.add("ChildFragment: initView type=$type isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")

        state.showLoading()
    }

    override fun lazyInit() {
        binding.textview.add("ChildFragment: lazyInit type=$type isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")
        lifecycleScope.delayed(2000) {
            state.showContent()
            delay(2000)
            state.showEmpty()
            delay(2000)
            state.showLoading()
            delay(2000)
            state.showRetry()
        }

        binding.textview.clickEvent {
            state.showLoading()
            lifecycleScope.delayed(2000) {
                state.showContent()
            }
        }
    }

    override fun initListener() {
        //点击重试
        state.onRetryClickListener = object : LoadStateView.OnRetryClickListener {
            override fun onRetryClick() {
                showToast("点击重试")
                lifecycleScope.delayed(2000) {
                    state.showContent()
                }
            }
        }
    }

    companion object {
        fun newInstance(type: String) = StateFragment1().apply {
            arguments = bundleOf("type" to type)
        }
    }
}