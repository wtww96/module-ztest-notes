package com.dp.notes.ui.tablayout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.dp.core.base.BaseActivity
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesActivityTablayoutNewBinding
import com.dp.notes.ui.lazy.ChildFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import com.google.android.material.tabs.TabLayoutMediator

/**
 * author Dq
 * date on 2023/5/17
 * description
 */
class TabLayoutNewActivity : BaseActivity(R.layout.notes_activity_tablayout_new) {
    private val binding by bindings<NotesActivityTablayoutNewBinding>()

    override fun initView(bundle: Bundle?) {
        val titles = arrayOf("关注1", "探索2", "新人新人新人33", "其他4", "关注5", "关注关注66", "关注7", "关注8")
        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = titles.size

            override fun createFragment(position: Int): Fragment {
                Log.e("hehe", "createFragment position=$position")
                return ChildFragment.newInstance(titles[position])
            }
        }

        val mediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            Log.e("hehe", "TabLayoutMediator position=$position")
            tab.text = titles[position]
            //tab.icon = com.dp.common.R.drawable.common_icon_test1.resToDrawable()
        }
        mediator.attach()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                Log.e("hehe", " onPageSelected  position=$position")
            }
        })
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: Tab?) {
                Log.e("hehe", " onTabSelected  tab=${tab?.position}")
            }

            override fun onTabUnselected(tab: Tab?) {
                Log.e("hehe", " onTabUnselected  tab=${tab?.position}")
            }

            override fun onTabReselected(tab: Tab?) {
                Log.e("hehe", " onTabReselected  tab=${tab?.position}")
            }
        })
        //binding.viewPager.setCurrentItem(1)
    }
}