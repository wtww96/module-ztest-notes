package com.dp.notes.ui.state

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.dp.common.state.EmptyPage
import com.dp.common.state.LoadingPage
import com.dp.core.base.BaseBindingActivity
import com.dp.core.extension.clickEvent
import com.dp.core.extension.delayed
import com.dp.core.extension.navigateTo
import com.dp.core.loadsir.SuccessState
import com.dp.notes.databinding.NotesActivityStateChildBinding
import kotlin.random.Random

/**
 * author Dq
 * date on 2023/6/2
 * description
 */
class StateChildActivity : BaseBindingActivity<NotesActivityStateChildBinding>() {

    override fun register() = binding.textView
    override fun initView(bundle: Bundle?) {
        //ErrorPage 只添加到临时缓存中
        //loadSir.registerStates(ErrorPage())
        lifecycleScope.delayed(1000) {
            loadSir.show<SuccessState>()
            //loadSir.show(SuccessState())
        }
    }

    override fun onReload() {
        Log.e("hehe", "四个按钮   空状态点击重新加了")
    }

    override fun initListener() {
        binding.btn1.clickEvent {
            val isFlag = Random.nextInt(10) % 2 == 0
            loadSir.show<LoadingPage>()
            lifecycleScope.delayed(2000) {
                if (isFlag) {
                    loadSir.show<EmptyPage>(useAnim = false)
                } else {
                    loadSir.show<SuccessState>()
                }
            }
        }

        binding.btn2.clickEvent {
            loadSir.show<EmptyPage>(Random.nextInt(3))
//            lifecycleScope.delayed(15000) {
//                loadSir.show<SuccessState>()
//            }
        }

        binding.btn3.clickEvent {
            //全局未配置,局部未配置,直接使用,反射,缓存
            //局部配置,直接使用
            loadSir.show<ErrorPage>()
        }

        binding.btn4.clickEvent {
            navigateTo<StateParentActivity>()
        }
    }
}