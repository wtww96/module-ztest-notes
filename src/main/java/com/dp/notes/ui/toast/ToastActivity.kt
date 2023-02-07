package com.dp.notes.ui.toast

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.dp.core.base.BaseActivity
import com.dp.core.extension.clickEvent
import com.dp.core.toast.ToastUtil
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesActivityToastBinding
import com.dp.notes.ui.hilt.TestHiltViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * author Dq
 * date on 2023/1/6
 * description Toast封装使用
 */
@AndroidEntryPoint
class ToastActivity : BaseActivity(R.layout.notes_activity_toast) {
    private val binding by bindings<NotesActivityToastBinding>()
    private val viewModel by viewModels<TestHiltViewModel>()

    override fun initView(bundle: Bundle?) {
        viewModel.rxRequest()
    }

    override fun initListener() {
        binding.bt1.clickEvent {
            Snackbar.make(binding.root, "fsdfsdfsd", Snackbar.LENGTH_LONG).show()
        }

        binding.bt2.clickEvent {
            ToastUtil.show("解放军卡萨丁就会计考试")
        }

        binding.bt3.clickEvent {
            ToastUtil.showLong("解放军卡萨丁就会计考试", 300)
        }

        binding.bt4.clickEvent {
            ToastUtil.show(R.string.notes_toastId)
        }

        binding.bt5.clickEvent {
            ToastUtil.show(this)
        }

        binding.bt6.clickEvent {
            ToastUtil.show("返回撒娇的九三接口sad吉安市凤凰山丢分就会发货的随机完全可结案时间跨度换肤大师激发我的骄傲是肯定就的省份黄金时代粉红色的阿代收代付")
        }

        binding.bt7.clickEvent {
            lifecycleScope.launch {
                ToastUtil.show("我是第一")
                delay(1000)
                ToastUtil.show("第二是他")
                delay(1000)
                ToastUtil.show("它是第三")
                delay(1000)
                ToastUtil.show("老四是谁")
                delay(1000)
                ToastUtil.show("没有老五")
            }
        }
    }
}