package com.dp.notes.ui.lazy

import com.dp.core.base.BaseFragment
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.FragmentTestBinding

/**
 * author Dq
 * date on 2022/9/19
 * description
 */
class FourFragment : BaseFragment(R.layout.fragment_test) {
    private val binding by bindings<FragmentTestBinding>()

    override fun initView() {
        binding.msg.text = "FourFourFourFour  Fragment"
        binding.textview.add("Four: initView-->isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")
    }

    override fun lazyInit() {
        binding.textview.add("Four: lazyInit-->isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")
    }

    override fun lazyResumeInit() {
        binding.textview.add("Four: lazyResumeInit-->isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")
    }

}