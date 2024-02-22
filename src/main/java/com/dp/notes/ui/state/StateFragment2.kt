package com.dp.notes.ui.state

import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.dp.common.state.EmptyPage
import com.dp.core.base.BaseFragment
import com.dp.core.extension.delayed
import com.dp.core.loadsir.SuccessState
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesFragmentState2Binding
import kotlin.random.Random

/**
 * author Dq
 * date on 2023/5/21
 * description
 */
class StateFragment2 : BaseFragment(R.layout.notes_fragment_state2) {
    private val binding by bindings<NotesFragmentState2Binding>()
    private var type = ""

    //override fun register() = binding.scrollview
    override fun registerRoot() = true

    override fun initView() {
        arguments?.let { type = it.getString("type", "") }
        binding.textview.clear()
        binding.textview.add("ChildFragment: initView type=$type isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")

        //适配SmartRefreshLayout,先removeView再setRefreshContent
        //或者scrollview再包一层
        //binding.refreshLayout.removeView(loadSir)
        //binding.refreshLayout.setRefreshContent(loadSir)
    }

    override fun lazyInit() {
        binding.textview.add("ChildFragment: lazyInit type=$type isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")

        lifecycleScope.delayed(1000) {
            val isFlag = Random.nextInt(10) % 2 == 0
            if (isFlag) {
                loadSir.show<EmptyPage>()
            } else {
                loadSir.show<SuccessState>()
            }
        }
    }

    override fun onReload() {
        Log.e("hehe", "StateFragment2 点击状态页重新加载")
    }

    companion object {
        fun newInstance(type: String) = StateFragment2().apply {
            arguments = bundleOf("type" to type)
        }
    }
}