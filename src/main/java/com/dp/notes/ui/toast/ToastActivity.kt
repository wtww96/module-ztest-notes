package com.dp.notes.ui.toast

import android.view.Gravity
import android.widget.Toast
import androidx.activity.viewModels
import com.dp.core.CoreManager
import com.dp.core.base.BaseActivity
import com.dp.core.extension.clickEvent
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.ActivityToastBinding
import com.dp.notes.ui.hilt.TestHiltViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


/**
 * author Dq
 * date on 2023/1/6
 * description Toast封装使用
 */
@AndroidEntryPoint
class ToastActivity : BaseActivity(R.layout.activity_toast) {
    private val binding by bindings<ActivityToastBinding>()
    private val viewModel by viewModels<TestHiltViewModel>()

    override fun initView() {
        viewModel.rxRequest()
    }

    override fun initListener() {
        binding.bt1.clickEvent {
            Snackbar.make(binding.root, "fsdfsdfsd", Snackbar.LENGTH_LONG).show()
        }

        binding.bt2.clickEvent {
            val toast = Toast.makeText(CoreManager.app, "xx", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, -200, -500)
            toast.show()
        }
    }
}