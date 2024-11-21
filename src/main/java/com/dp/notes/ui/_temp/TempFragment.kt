package com.dp.notes.ui._temp

import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.dp.common.widget.tablayout.SlidingAdapter
import com.dp.core.base.BaseFragment
import com.dp.core.extension.cacheSize
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesFragmentChildBinding
import com.dp.notes.databinding.NotesFragmentTempBinding
import com.dp.notes.ui.lazy.ChildFragment

/**
 * author Dq
 * date on 2024/11/7
 * description
 */
class TempFragment : BaseFragment(R.layout.notes_fragment_temp) {
    private val binding by bindings<NotesFragmentTempBinding>()

    override fun initView() {
        val titles = arrayOf("qwe", "asd", "zxc", "rty")
        binding.viewPager.offscreenPageLimit = titles.size
        val fragmentList = arrayListOf<Fragment>()
        titles.forEach {
            fragmentList.add(TempChildFragment().apply {
                arguments = bundleOf("type" to it)
            })
        }

        binding.viewPager.adapter = object :
            FragmentPagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment {
                return fragmentList[position]
            }

            override fun getCount(): Int {
                return fragmentList.size
            }

            override fun getPageTitle(position: Int): CharSequence {
                return titles[position]
            }

            override fun getItemPosition(`object`: Any): Int {
                return PagerAdapter.POSITION_NONE
            }
        }
        binding.tabLayout.setViewPager(binding.viewPager)
    }
}