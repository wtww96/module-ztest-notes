package com.dp.notes.ui.lazy

import android.util.Log
import com.dp.core.base.BaseFragment
import com.dp.core.extension.clickEvent
import com.dp.core.viewbinding.bindings
import com.dp.core.windowinsets.*
import com.dp.notes.R
import com.dp.notes.databinding.NotesFragmentTestBinding

/**
 * author Dq
 * date on 2022/9/19
 * description
 */
class ThreeFragment : BaseFragment(R.layout.notes_fragment_test) {
    private val binding by bindings<NotesFragmentTestBinding>()

    override fun initView() {
        binding.msg.text = "ThreeThreeThree  Fragment"
        binding.textview.add("Three: initView-->isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")
    }

    override fun lazyInit() {
        binding.textview.add("Three: lazyInit-->isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")
    }

    override fun lazyResumeInit() {
        binding.textview.add("Three: lazyResumeInit-->isResumed=$isResumed , isVisible=$isVisible , isHidden=$isHidden")
    }

    override fun initListener() {
        binding.button.clickEvent {
            Log.e("hehe", "Fragment ime isVisible = ${imeVisible()}")
        }

        ImeHelper.addImeChangeCallback(this) {
            onStatus {
            }
        }
    }
}