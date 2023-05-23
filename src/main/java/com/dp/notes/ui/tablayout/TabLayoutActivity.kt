package com.dp.notes.ui.tablayout

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.dp.common.widget.tablayout.SlidingAdapter
import com.dp.core.base.BaseActivity
import com.dp.core.extension.cacheSize
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesActivityTablayoutBinding
import com.dp.notes.ui.lazy.ChildFragment

/**
 * author Dq
 * date on 2023/5/17
 * description
 */
class TabLayoutActivity : BaseActivity(R.layout.notes_activity_tablayout) {
    private val binding by bindings<NotesActivityTablayoutBinding>()

    override fun initView(bundle: Bundle?) {
        val titles = arrayOf("关注1", "探索2", "新人3", "其他4", "关注5")
        binding.viewPager.cacheSize = 3
        binding.viewPager.adapter = object : SlidingAdapter(this) {
            override fun getItemCount(): Int = titles.size

            override fun createFragment(position: Int): Fragment {
                return ChildFragment.newInstance(titles[position])
            }

            override fun getPageTitle(position: Int): CharSequence {
                return titles[position]
            }
        }
        binding.tabLayout.setViewPager(binding.viewPager)
    }
}