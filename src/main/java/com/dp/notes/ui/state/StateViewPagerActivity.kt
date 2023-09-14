package com.dp.notes.ui.state

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.dp.common.widget.tablayout.SlidingAdapter
import com.dp.core.base.BaseActivity
import com.dp.core.extension.cacheSize
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesActivityStateViewpagerBinding

/**
 * author Dq
 * date on 2023/5/21
 * description 页面状态管理---ViewPager2+Fragment+RecyclerView
 */
class StateViewPagerActivity : BaseActivity(R.layout.notes_activity_state_viewpager) {
    private val binding by bindings<NotesActivityStateViewpagerBinding>()

    override fun initView(bundle: Bundle?) {
        val titles = arrayOf("关注1", "探索2", "新人3", "其他4", "关注5")
        (binding.viewPager.getChildAt(0) as RecyclerView).overScrollMode = View.OVER_SCROLL_NEVER
        binding.viewPager.cacheSize = titles.size
        binding.viewPager.adapter = object : SlidingAdapter(this) {
            override fun getItemCount(): Int = titles.size

            override fun createFragment(position: Int): Fragment {
                return StateFragment2.newInstance(titles[position])
            }

            override fun getPageTitle(position: Int): CharSequence {
                return titles[position]
            }
        }
        binding.tabLayout.setViewPager(binding.viewPager)
    }
}