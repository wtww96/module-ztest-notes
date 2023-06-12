package com.dp.notes.ui.state

import com.dp.core.base.BaseActivity
import com.dp.core.extension.clickEvent
import com.dp.core.extension.navigateTo
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesActivityStateBinding

/**
 * author Dq
 * date on 2023/5/21
 * description 页面状态管理
 */
class StateActivity : BaseActivity(R.layout.notes_activity_state) {
    private val binding by bindings<NotesActivityStateBinding>()

    override fun initListener() {
        binding.btn1.clickEvent {
            navigateTo<StateParentActivity>()
        }
        binding.btn2.clickEvent {
            navigateTo<StateChildActivity>()
        }
        binding.btn3.clickEvent {
            navigateTo<StateViewPagerActivity>()
        }
    }
}