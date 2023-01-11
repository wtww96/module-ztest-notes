package com.dp.notes.ui

import com.dp.core.base.BaseFragment
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesFragmentTestBinding

/**
 * author Dq
 * date on 2022/9/14
 * description
 */
class TestFragment : BaseFragment(R.layout.notes_fragment_test) {
    private val binding by bindings<NotesFragmentTestBinding>()

    override fun initView() {
    }

    override fun initListener() {
    }
}