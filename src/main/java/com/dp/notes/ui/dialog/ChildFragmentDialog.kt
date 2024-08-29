package com.dp.notes.ui.dialog

import android.app.Dialog
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import com.dp.core.base.BaseFragmentDialog
import com.dp.core.extension.clickEvent
import com.dp.core.extension.dp
import com.dp.notes.databinding.NotesDialogTestBinding

/**
 * author Dq
 * date on 2022/10/19
 * description
 */
class ChildFragmentDialog : BaseFragmentDialog<NotesDialogTestBinding>() {

    override fun initView() {
        binding.button.text = "加上健康登记卡数据库"
        binding.button.clickEvent { dismiss() }

        dialog?.setOnKeyListener { _, keyCode, event ->
            Log.e("hehe", " setOnKeyListener keyCode = $keyCode")
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                dialog?.let {
                    Log.e("hehe", " setOnKeyListener ${it.isShowing}")
                    if (it.isShowing) {
                        it.hide()
                    }
                }
                true
            } else {
                false
            }
        }

        // 设置弹窗为可以取消，当用户点击弹窗外部时取消弹窗
        dialog?.setCanceledOnTouchOutside(true)
        // 监听取消事件，取消时只隐藏弹窗
        dialog?.setOnCancelListener {
            (it as Dialog).hide()
        }
    }

    override fun onPause() {
        super.onPause()
        Log.e("hehe", " onPause ")
    }

    override fun onStop() {
        super.onStop()
        Log.e("hehe", " onStop ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("hehe", " onDestroyView ")
    }

    override fun dialogGravity(): Int = Gravity.BOTTOM
    override fun dimAmount(): Float = 0f
//    override fun dialogHeight(): Int = LayoutParams.MATCH_PARENT
//    override fun dialogHeight(): Int = LayoutParams.WRAP_CONTENT
    override fun dialogHeight(): Int = 500.dp

    companion object {
        fun show(any: Any) {
            ChildFragmentDialog().show(any)
        }
    }
}