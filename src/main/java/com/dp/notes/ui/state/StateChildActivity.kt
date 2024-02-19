package com.dp.notes.ui.state

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.dp.core.base.BaseActivity
import com.dp.core.extension.clickEvent
import com.dp.core.extension.delayed
import com.dp.core.loadsir.LoadLayout
import com.dp.core.loadsir.LoadSir
import com.dp.common.state.EmptyPage
import com.dp.common.state.LoadingPage
import com.dp.core.extension.navigateTo
import com.dp.core.loadsir.SuccessState
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesActivityStateChildBinding
import kotlin.random.Random

/**
 * author Dq
 * date on 2023/6/2
 * description
 */
class StateChildActivity : BaseActivity(R.layout.notes_activity_state_child) {
    private val binding by bindings<NotesActivityStateChildBinding>()
    private lateinit var loadSir: LoadLayout

    override fun initView(bundle: Bundle?) {
        loadSir = LoadSir.register(binding.textView) {
            Log.e("hehe", "空状态点击重新加了")
        }
        lifecycleScope.delayed(1000) {
            loadSir.show<SuccessState>()
            //loadSir.show(SuccessState())
        }
    }

    override fun initListener() {
        binding.btn1.clickEvent {
            val isFlag = Random.nextInt(10) % 2 == 0
            loadSir.show<LoadingPage>()
            lifecycleScope.delayed(2000) {
                if (isFlag) {
                    loadSir.show<EmptyPage>(false)
                } else {
                    loadSir.show<SuccessState>()
                }
            }
        }

        binding.btn2.clickEvent {
            loadSir.show<EmptyPage>()
//            lifecycleScope.delayed(15000) {
//                loadSir.show<SuccessState>()
//            }
        }

        binding.btn3.clickEvent {
            navigateTo<StateParentActivity>()
        }
    }
}