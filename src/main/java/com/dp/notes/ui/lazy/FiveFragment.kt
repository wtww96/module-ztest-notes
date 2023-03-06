package com.dp.notes.ui.lazy

import com.dp.core.base.BaseFragment
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesFragmentTestBinding

/**
 * author Dq
 * date on 2022/9/19
 * description
 */
class FiveFragment : BaseFragment(R.layout.notes_fragment_test) {
    private val binding by bindings<NotesFragmentTestBinding>()

    override fun initView() {
        binding.msg.text = "FiveFiveFiveFiveFive  Fragment"
        binding.textview.add("Five: initView-->isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")
    }

    override fun lazyInit() {
        binding.textview.add("Five: lazyInit-->isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")
    }

    override fun lazyResume() {
        binding.textview.add("Five: lazyResumeInit-->isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")
    }
}