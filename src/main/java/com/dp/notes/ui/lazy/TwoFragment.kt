package com.dp.notes.ui.lazy

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.dp.core.base.BaseFragment
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesFragmentTwoBinding

/**
 * author Dq
 * date on 2022/9/19
 * description
 */
class TwoFragment : BaseFragment(R.layout.notes_fragment_two) {
    private val binding by bindings<NotesFragmentTwoBinding>()

    override fun initView() {
        binding.textview.add("Two: initView-->isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")
    }

    override fun lazyInit() {
        binding.textview.add("Two: lazyInit-->isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")
        val titles = arrayOf("关注", "探索", "新人", "其他", "关注", "探索", "新人", "其他")
        binding.viewPager.offscreenPageLimit = 2
        binding.viewPager.adapter = object : FragmentPagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getCount(): Int = titles.size

            override fun getItem(position: Int): Fragment {
                return ChildFragment.newInstance(position)
            }

            override fun getPageTitle(position: Int): CharSequence = titles[position]
        }
        binding.tabLayout.setViewPager(binding.viewPager)
        binding.viewPager.setCurrentItem(1, false)
    }

    override fun lazyResumeInit() {
        binding.textview.add("Two: lazyResumeInit-->isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")
    }
}