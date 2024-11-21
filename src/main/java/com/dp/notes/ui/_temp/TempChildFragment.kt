package com.dp.notes.ui._temp

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dp.core.base.BaseFragment
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesFragmentTempChildBinding
import com.dp.notes.ui.lazy.ChildFragment
import com.google.android.material.tabs.TabLayoutMediator

/**
 * author Dq
 * date on 2024/11/7
 * description
 */
class TempChildFragment : BaseFragment(R.layout.notes_fragment_temp_child) {
    private val binding by bindings<NotesFragmentTempChildBinding>()

    override fun initView() {
        Log.e("hehe", ">>>>>>>>>>>>>>> initView    type=${arguments?.getString("type")}")
        if (arguments?.getString("type") != "asd") return
        Log.e("hehe", ">>>>>>>>>>>>>>> initView    size=${childFragmentManager.fragments.size}    ${arguments?.getString("type")}")

        val titles = arrayOf("关注1", "探索2")
        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = titles.size

            override fun createFragment(position: Int): Fragment {
                return ChildFragment.newInstance(titles[position])
            }
        }

        val mediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }
        mediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("hehe", ">>>>>>>>>>>>>>> onDestroyView    size=${childFragmentManager.fragments.size}")
    }
}