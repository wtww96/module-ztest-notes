package com.dp.notes.ui.state

import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.dp.core.base.BaseFragment
import com.dp.core.extension.clickEvent
import com.dp.core.extension.delayed
import com.dp.core.loadsir.LoadLayout
import com.dp.core.loadsir.LoadSir
import com.dp.core.loadsir.state.EmptyPage
import com.dp.core.loadsir.state.LoadingPage
import com.dp.core.loadsir.state.SuccessState
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesFragmentState1Binding
import kotlin.random.Random

/**
 * author Dq
 * date on 2023/5/21
 * description
 */
class StateFragment1 : BaseFragment(R.layout.notes_fragment_state1) {
    private val binding by bindings<NotesFragmentState1Binding>()
    private lateinit var loadSir: LoadLayout
    private var type = ""

    override fun initView() {
        arguments?.let { type = it.getString("type", "") }
        binding.textview.clear()
        binding.textview.add("ChildFragment: initView type=$type isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")

        loadSir = LoadSir.register(binding.textview)
    }

    override fun lazyInit() {
        binding.textview.add("ChildFragment: lazyInit type=$type isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")

        lifecycleScope.delayed(1000) {
            loadSir.show<SuccessState>()
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
            lifecycleScope.delayed(5000) {
                loadSir.show<SuccessState>()
            }
        }
    }

    companion object {
        fun newInstance(type: String) = StateFragment1().apply {
            arguments = bundleOf("type" to type)
        }
    }
}