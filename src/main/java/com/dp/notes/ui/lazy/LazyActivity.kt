package com.dp.notes.ui.lazy

import com.dp.core.base.BaseActivity
import com.dp.core.extension.bindAdapter
import com.dp.core.extension.cacheSize
import com.dp.core.extension.clickEvent
import com.dp.core.viewbinding.bindings
import com.dp.notes.databinding.ActivityLazyBinding

/**
 * author Dq
 * date on 2022/9/16
 * description
 */
class LazyActivity : BaseActivity() {
    private val binding by bindings<ActivityLazyBinding>()

    override fun getLayoutView() = binding.root

    override fun initView() {
        initFragment()
    }

    override fun initListener() {
        binding.one.clickEvent { setCurrentTab(0) }
        binding.two.clickEvent { setCurrentTab(1) }
        binding.three.clickEvent { setCurrentTab(2) }
        binding.four.clickEvent { setCurrentTab(3) }
        binding.five.clickEvent { setCurrentTab(4) }
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
            isUserInputEnabled = false
        }
    }

    /***
     * Fragment切换
     */
    private fun setCurrentTab(position: Int) {
        binding.viewpager.setCurrentItem(position, false)
    }
}