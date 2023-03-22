package com.dp.notes.ui.lazy

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dp.core.base.BaseFragment
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesFragmentThreeBinding
import com.google.android.material.tabs.TabLayoutMediator


/**
 * author Dq
 * date on 2022/9/19
 * description
 */
class ThreeFragment : BaseFragment(R.layout.notes_fragment_three) {
    private val binding by bindings<NotesFragmentThreeBinding>()

    override fun initView() {
    }

    override fun lazyInit() {
        val tabs = arrayOf("关注", "推荐", "最新0", "最新1", "最新2", "最新3", "最新4", "最新5", "最新6")
        //binding.viewPager.offscreenPageLimit = tabs.size
        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = tabs.size

            override fun createFragment(position: Int): Fragment {
                Log.e("hehe", "createFragment position=$position")
                return ChildFragment.newInstance(tabs[position])
            }
        }

        val mediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            Log.e("hehe", "TabLayoutMediator position=$position")
            tab.text = tabs[position]
        }
        mediator.attach()
    }

    override fun lazyResume() {
    }

    override fun initListener() {

    }
}