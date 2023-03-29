package com.dp.notes.ui.lazy

import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.dp.core.base.BaseActivity
import com.dp.core.extension.bindAdapter
import com.dp.core.extension.cacheSize
import com.dp.core.extension.clickEvent
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesActivityLazyBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * author Dq
 * date on 2022/9/16
 * description
 */
@AndroidEntryPoint
class LazyActivity : BaseActivity(R.layout.notes_activity_lazy) {
    private val binding by bindings<NotesActivityLazyBinding>()

    override fun initView(bundle: Bundle?) {
        initFragment()
    }

    override fun initListener() {
        binding.one.clickEvent { setCurrentTab(0) }
        binding.two.clickEvent { setCurrentTab(1) }
        binding.three.clickEvent { setCurrentTab(2) }
        binding.four.clickEvent { setCurrentTab(3) }
        binding.five.clickEvent { setCurrentTab(4) }

        binding.viewpager.registerOnPageChangeCallback(object :OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                Log.e("hehe","onPageSelected, position = $position")
            }
        })
    }

    private fun initFragment() {
        binding.viewpager.bindAdapter(this, 5) {
            when (it) {
                0 -> OneFragment()
                1 -> TwoFragment()
                2 -> ThreeFragment()
                3 -> FourFragment()
                else -> FiveFragment()
            }
        }.apply {
            cacheSize = 5
            offscreenPageLimit = 5
            isUserInputEnabled = false
        }
        binding.viewpager.setCurrentItem(1,false)
    }

    /***
     * Fragment切换
     */
    private fun setCurrentTab(position: Int) {
        binding.viewpager.setCurrentItem(position, false)
    }
}