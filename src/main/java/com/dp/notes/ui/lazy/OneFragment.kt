package com.dp.notes.ui.lazy

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.dp.core.base.BaseFragment
import com.dp.core.extension.cacheSize
import com.dp.core.network.launchIn
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesFragmentOneBinding
import com.dp.notes.ui.http.HttpViewModel
import com.dp.notes.widget.tablayout.SlidingAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * author Dq
 * date on 2022/9/19
 * description
 */
@AndroidEntryPoint
class OneFragment : BaseFragment(R.layout.notes_fragment_one) {
    private val binding by bindings<NotesFragmentOneBinding>()
    private val viewModel by viewModels<HttpViewModel>()

    override fun initView() {
        binding.textview.add("One: initView-->isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")
    }

    override fun lazyInit() {
        binding.textview.add("One: lazyInit-->isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")
        val titles = arrayOf("关注1", "探索2", "新人3", "其他4", "关注5")
        (binding.viewPager.getChildAt(0) as RecyclerView).overScrollMode = View.OVER_SCROLL_NEVER
        binding.viewPager.offscreenPageLimit = titles.size
        binding.viewPager.cacheSize = 3
        binding.viewPager.adapter = object : SlidingAdapter(this) {
            override fun getItemCount(): Int = titles.size

            override fun createFragment(position: Int): Fragment {
                return ChildFragment.newInstance(titles[position])
            }

            override fun getPageTitle(position: Int): CharSequence {
                return  titles[position]
            }
        }
        binding.tabLayout.setViewPager(binding.viewPager)
        binding.tabLayout.setCurrentTab(1)
    }

    override fun lazyResume() {
        binding.textview.add("One: lazyResumeInit-->isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")
    }

    override fun initObserve() {
        viewModel.flowRequest2().launchIn(this) {

        }
    }
}