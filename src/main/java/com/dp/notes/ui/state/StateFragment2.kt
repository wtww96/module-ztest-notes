package com.dp.notes.ui.state

import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.dp.core.base.BaseFragment
import com.dp.core.extension.delayed
import com.dp.core.loadsir.LoadLayout
import com.dp.core.loadsir.LoadSir
import com.dp.core.loadsir.SuccessState
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesFragmentState2Binding

/**
 * author Dq
 * date on 2023/5/21
 * description
 */
class StateFragment2 : BaseFragment(R.layout.notes_fragment_state2) {
    private val binding by bindings<NotesFragmentState2Binding>()
    private lateinit var loadSir: LoadLayout
    private var type = ""

    override fun initView() {
        arguments?.let { type = it.getString("type", "") }
        binding.textview.clear()
        binding.textview.add("ChildFragment: initView type=$type isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")

        loadSir = LoadSir.register(binding.scrollview)
        //适配SmartRefreshLayout,先removeView再setRefreshContent
        //或者scrollview再包一层
        binding.refreshLayout.removeView(loadSir)
        binding.refreshLayout.setRefreshContent(loadSir)
    }

    override fun lazyInit() {
        binding.textview.add("ChildFragment: lazyInit type=$type isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")

        lifecycleScope.delayed(1000) {
            loadSir.show<SuccessState>()
        }
    }

    override fun initListener() {
    }

    companion object {
        fun newInstance(type: String) = StateFragment2().apply {
            arguments = bundleOf("type" to type)
        }
    }
}