package com.dp.notes.ui.state

import com.dp.core.loadsir.test.BaseActivityX
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesActivityStateChildBinding

/**
 * author Dq
 * date on 2023/6/2
 * description
 */
class StateChildActivity : BaseActivityX(R.layout.notes_activity_state_child) {
    private val binding by bindings<NotesActivityStateChildBinding>()

    override fun initListener() {
    }
}