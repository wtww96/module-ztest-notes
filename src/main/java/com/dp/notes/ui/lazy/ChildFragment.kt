package com.dp.notes.ui.lazy

import androidx.core.os.bundleOf
import com.dp.core.base.BaseFragment
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.FragmentChildBinding

/**
 * author Dq
 * date on 2022/9/19
 * description
 */
class ChildFragment : BaseFragment(R.layout.fragment_child) {
    private val binding by bindings<FragmentChildBinding>()

    private var type = 0

    override fun initView() {
        arguments?.let { type = it.getInt("type") }
        binding.textview.clear()
        binding.textview.add("ChildFragment: initView type=$type isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")
    }

    override fun lazyInit() {
        binding.textview.add("ChildFragment: lazyInit type=$type isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")
    }

    override fun onPause() {
        super.onPause()
        binding.textview.add("ChildFragment: onPause type=$type isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")
    }

    override fun onStop() {
        super.onStop()
        binding.textview.add("ChildFragment: onStop type=$type isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.textview.add("ChildFragment: onDestroyView type=$type isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")
    }

    override fun onDestroy() {
        super.onDestroy()
        //todo 此时binding已经被销毁
        //binding.textview.add("ChildFragment: onDestroy type=$type isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")
    }

    companion object {
        fun newInstance(type: Int) = ChildFragment().apply {
            arguments = bundleOf("type" to type)
        }
    }
}