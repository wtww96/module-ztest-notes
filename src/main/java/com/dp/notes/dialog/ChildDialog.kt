package com.dp.notes.dialog

import android.view.Gravity
import com.dp.core.base.BaseFragmentDialog
import com.dp.notes.R
import com.dp.notes.databinding.DialogTest1Binding
import com.dp.core.extension.clickEvent
import com.dp.core.viewbinding.bindings

/**
 * author Dq
 * date on 2022/10/19
 * description
 */
class ChildDialog : BaseFragmentDialog(R.layout.dialog_test1) {
    private val binding by bindings<DialogTest1Binding>()

    override fun initView() {
        binding.button.text = "加上健康登记卡数据库"
        binding.button.clickEvent { dismiss() }
    }

    override fun dialogGravity(): Int = Gravity.BOTTOM
    override fun dialogAnim(): Int = com.dp.core.R.style.AnimBottom

    companion object {
        fun show(any: Any) {
            ChildDialog().show(any)
        }
    }
}