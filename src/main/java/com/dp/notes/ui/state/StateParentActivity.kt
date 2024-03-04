package com.dp.notes.ui.state

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.dp.common.state.EmptyPage
import com.dp.common.state.LoadingPage
import com.dp.core.base.BaseBindingActivity
import com.dp.core.extension.clickEvent
import com.dp.core.extension.delayed
import com.dp.core.loadsir.SuccessState
import com.dp.notes.databinding.NotesActivityStateParentBinding
import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * author Dq
 * date on 2023/6/2
 * description
 */
class StateParentActivity : BaseBindingActivity<NotesActivityStateParentBinding>() {

    override fun register() = this
    override fun initView(bundle: Bundle?) {
        //loadSir.show<SuccessState>()
        //loadSir.show<LoadingPage>()
        lifecycleScope.delayed(1000) {
            loadSir.show<SuccessState>()
        }
    }

    override fun onReload() {
        Log.e("hehe", "两个按钮   空状态点击重新加了")
    }

    override fun initListener() {
        binding.btn1.clickEvent {
            loadSir.show<LoadingPage>()
            lifecycleScope.delayed(2000) {
                loadSir.show<EmptyPage>(useAnim = false).apply {
                    delay(2000)
                    loadSir.show<SuccessState>()
                }
            }
        }

        binding.btn2.clickEvent {
            loadSir.show<EmptyPage> { it.updateView(Random.nextInt(5, 8)) }
            lifecycleScope.delayed(3000) {
                loadSir.show<SuccessState>()
            }
        }
    }
}