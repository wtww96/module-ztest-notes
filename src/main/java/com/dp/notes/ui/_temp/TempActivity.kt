package com.dp.notes.ui._temp

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.dp.core.base.BaseActivity
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesActivityTempBinding

/**
 * author Dq
 * date on 2024/11/7
 * description
 */
class TempActivity : BaseActivity(R.layout.notes_activity_temp) {
    private val binding by bindings<NotesActivityTempBinding>()

    override fun initView(bundle: Bundle?) {
        val titles = arrayOf("关注1", "探索2", "新人3", "其他4", "关注5")
        binding.viewPager.offscreenPageLimit = titles.size
        val fragmentList = arrayListOf<Fragment>()
        titles.forEach { fragmentList.add(TempFragment()) }
        binding.viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getCount() = fragmentList.size

            override fun getItem(position: Int) = fragmentList[position]

            override fun getPageTitle(position: Int): CharSequence {
                return titles[position]
            }
        }

        binding.tabLayout.setViewPager(binding.viewPager, titles)
    }
}