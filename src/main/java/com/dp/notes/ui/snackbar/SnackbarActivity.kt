package com.dp.notes.ui.snackbar

import android.util.Log
import androidx.activity.viewModels
import com.dp.core.base.BaseActivity
import com.dp.core.extension.clickEvent
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.ActivitySnackbarBinding
import com.dp.notes.ui.hilt.TestHiltViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


/**
 * author Dq
 * date on 2023/1/6
 * description Snackbar 代替 Toast
 */
@AndroidEntryPoint
class SnackbarActivity : BaseActivity(R.layout.activity_snackbar) {
    private val binding by bindings<ActivitySnackbarBinding>()
    private val viewModel by viewModels<TestHiltViewModel>()

    override fun initView() {
        viewModel.rxRequest()
    }

    override fun initListener() {
        binding.bt1.clickEvent {
            Log.e("hehe", "Snackbar.make")
            Snackbar.make(binding.root, "fsdfsdfsd", Snackbar.LENGTH_LONG).show()
        }
    }
}