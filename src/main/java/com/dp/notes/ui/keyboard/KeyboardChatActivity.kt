package com.dp.notes.ui.keyboard

import android.util.Log
import android.view.View
import androidx.core.view.ViewCompat
import com.dp.core.base.BaseActivity
import com.dp.core.extension.clickEvent
import com.dp.core.extension.isEmpty
import com.dp.core.viewbinding.bindings
import com.dp.core.windowinsets.*
import com.dp.notes.R
import com.dp.notes.databinding.ActivityKeyboardChatBinding

/**
 * author Dq
 * date on 2022/11/25
 * description 即时聊天页面软键盘布局
 */
class KeyboardChatActivity : BaseActivity(R.layout.activity_keyboard_chat) {
    private val binding by bindings<ActivityKeyboardChatBinding>()

    override fun initView() {
        binding.title.fitStatusBar(true)
        binding.recyclerView.adapter = ConversationAdapter()
        binding.tvContent.text = "软键盘状态 : ${if (imeVisible()) "显示" else "隐藏"}"
        //-------------------------------------
        ImeHelper.addImeChangeCallback(this) {
            onStatus {
                binding.tvContent.text = "软键盘状态${if (it) "显示" else "隐藏"} : ${context.imeVisible()}"
            }
        }

        binding.root.addImeAnimationCallback {
            onHeight {
                binding.inputLinear.root.translationY = -it
                binding.recyclerView.translationY = -it
                binding.line.translationY = -it
            }
            onStatus {
                Log.e("hehe", " 软键盘当前状态显示状态 = $it")
                if (it) {
                    binding.inputLinear.etContent.requestFocus()
                } else {
                    binding.inputLinear.etContent.clearFocus()
                }
            }
        }
    }

    override fun initListener() {
        //发送
        //1.输入框有内容:直接发送,不变更软键盘的显示隐藏状态
        //2.输入框没内容:获取焦点,显示软键盘
        //注意:发送输入框内容不变更软键盘的显示隐藏状态
        binding.inputLinear.btSend.clickEvent {
            if (binding.inputLinear.etContent.isEmpty) {
                showSoftInput(binding.inputLinear.etContent)
            } else {
                Log.e("hehe", "******************* 发送 *********************** ")
            }
        }
    }

    fun addKeyBordHeightChangeCallBack(view: View, callback: WindowInsetsAnimationCallback) {
        ViewCompat.setOnApplyWindowInsetsListener(view, callback)
        //动画回调,必须要开启沉浸式模式-->WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setWindowInsetsAnimationCallback(view, callback)
    }
}