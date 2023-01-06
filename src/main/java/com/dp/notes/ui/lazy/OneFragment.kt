package com.dp.notes.ui.lazy

import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dp.core.base.BaseFragment
import com.dp.core.extension.cacheSize
import com.dp.core.network.launchIn
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.FragmentOneBinding
import com.dp.notes.ui.http.HttpViewModel
import com.dp.notes.widget.tablayout.SlidingAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * author Dq
 * date on 2022/9/19
 * description
 */
@AndroidEntryPoint
class OneFragment : BaseFragment(R.layout.fragment_one) {
    private val binding by bindings<FragmentOneBinding>()
    private val viewModel by viewModels<HttpViewModel>()

    override fun initView() {
        binding.textview.add("One: initView-->isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")
    }

    override fun lazyInit() {
        binding.textview.add("One: lazyInit-->isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")
        val titles = arrayOf("关注", "探索", "新人", "其他", "关注", "探索", "新人", "其他")
        binding.viewPager.cacheSize = 3
        binding.viewPager.adapter = object : SlidingAdapter(this) {
            override fun getItemCount(): Int = titles.size

            override fun createFragment(position: Int): Fragment {
                return ChildFragment.newInstance(position)
            }

            override fun getPageTitle(position: Int): CharSequence = titles[position]
        }
        binding.tabLayout.setViewPager(binding.viewPager)
        binding.viewPager.setCurrentItem(1, false)
    }

    override fun lazyResumeInit() {
        binding.textview.add("One: lazyResumeInit-->isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")
    }

    override fun initObserve() {
        viewModel.flowRequest2().launchIn(this) {

        }
    }
}