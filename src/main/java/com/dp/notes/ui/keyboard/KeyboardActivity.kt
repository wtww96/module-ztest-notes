package com.dp.notes.ui.keyboard

import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import com.alibaba.android.arouter.facade.annotation.Route
import com.dp.common.route.PageRoute
import com.dp.core.base.BaseActivity
import com.dp.core.extension.bindStub
import com.dp.core.extension.clickEvent
import com.dp.core.extension.navigateTo
import com.dp.core.viewbinding.bindings
import com.dp.core.windowinsets.*
import com.dp.notes.R
import com.dp.notes.databinding.ActivityKeyboardBinding
import com.dp.notes.databinding.LayoutInputBinding

/**
 * author Dq
 * date on 2022/11/25
 * description 软键盘相关操作
 */
@Route(path = PageRoute.ACTIVITY_NOTES_KEYBOARD)
class KeyboardActivity : BaseActivity(R.layout.activity_keyboard) {
    private val binding by bindings<ActivityKeyboardBinding>()
    private var stubBinding: LayoutInputBinding? = null

    override fun initView() {
        binding.tvContent.fitStatusBar(true)
        binding.tvContent.text = "软键盘状态 : ${if (imeVisible()) "显示" else "隐藏"}"
        //------------------------
        ImeHelper.addImeChangeCallback(this) {
            onHeight {
                Log.e("hehe", "addImeChangeCallback onHeight=$it")
                //stubBinding?.root?.translationY = -it
                //binding.line.translationY = -it
            }
            onStatus {
                binding.bt1.text = "软键盘状态:${if (it) "显示" else "隐藏"}"
                binding.tvContent.text = "${binding.bt1.text} : ${context.imeVisible()}"
            }
        }
        binding.root.addImeAnimationCallback {
            onHeight {
                //Log.e("hehe", "addImeAnimationCallback onHeight=$it")
                stubBinding?.root?.translationY = -it
                binding.line.translationY = -it
            }
            onStatus {

            }
        }
    }

    override fun initListener() {
        //输入框显示隐藏
        binding.bt1.clickEvent {
            if (!imeVisible()) {
                showSoftInput(binding.et1)
            } else {
                hideSoftInput(binding.et1)
            }
        }

        //即时聊天底部输入框动画
        binding.bt2.clickEvent {
            navigateTo<KeyboardChatActivity>()
        }

        //从底部弹出输入框+软键盘
        binding.bt3.clickEvent {
            initViewStub(true)
            showSoftInput(stubBinding?.etContent)
        }
    }

    private fun initViewStub(visible: Boolean) {
        if (visible) {
            stubBinding = stubBinding.bindStub(binding.viewStub)
        } else {
            stubBinding?.root?.isVisible = false
        }
    }
}
