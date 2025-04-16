package com.dp.notes.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.PixelFormat
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.WindowManager
import android.widget.FrameLayout
import com.dp.core.base.BaseFragmentDialog
import com.dp.core.extension.clickEvent
import com.dp.core.windowinsets.statusHeight
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

        binding.tvBtn.clickEvent {
            //moveButtonToRootView()
            //moveButtonToDialogWindow()

            binding.tvBtn.animate()
                .rotation(360f)
                .setDuration(20000L) // 平滑过渡时间
                .start()

            Handler().postDelayed({
            moveButtonToWindow()
            },5000)
        }
    }

    private fun moveButtonToRootView() {
        val rootView = activity?.window?.decorView as? ViewGroup ?: return

        // 获取按钮在屏幕中的绝对位置
        val location = IntArray(2)
        binding.tvBtn.getLocationOnScreen(location)

        Log.e("hehe", "location[0]=${location[0]} , location[1]=${location[1]}   parent=${binding.tvBtn.parent}")

        // 创建布局参数，保持按钮在根视图中的位置一致
        val layoutParams = FrameLayout.LayoutParams(binding.tvBtn.width, binding.tvBtn.height).apply {
            leftMargin = location[0]
            topMargin = location[1]
        }

        // 从原父布局中移除按钮
        //binding.tvBtn.alpha = 0f
        (binding.tvBtn.parent as? ViewGroup)?.removeView(binding.tvBtn)
        rootView.addView(binding.tvBtn, layoutParams)

//        // 添加过渡动画
//        binding.tvBtn.animate()
//            .translationY(0f)
//            .setDuration(300L) // 平滑过渡时间
//            .start()

        Handler().post { dismiss() }
    }

    private fun moveButtonToWindow() {
        val location = IntArray(2)
        binding.tvBtn.getLocationOnScreen(location) // 获取按钮的屏幕坐标

        Log.e("hehe", "location[0]=${location[0]} , location[1]=${location[1]}")

        // 设置按钮的位置参数
        val params = WindowManager.LayoutParams().apply {
//            width = binding.tvBtn.width
//            height = binding.tvBtn.height
            width = -2
            height = -2
            x = location[0]
            y = location[1] - statusHeight

            type = WindowManager.LayoutParams.TYPE_APPLICATION  // 或 TYPE_APPLICATION
            format = PixelFormat.TRANSLUCENT
            gravity = Gravity.TOP or Gravity.START
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        }

        (binding.tvBtn.parent as? ViewGroup)?.removeView(binding.tvBtn)
        // 添加到窗口管理器
        val windowManager = requireActivity().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.addView(binding.tvBtn, params)

        // 关闭 DialogFragment
        Handler().post {
            dismiss()
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
    override fun dialogHeight(): Int = LayoutParams.WRAP_CONTENT
//    override fun dialogHeight(): Int = LayoutParams.MATCH_PARENT
//    override fun dialogHeight(): Int = 500.dp

    companion object {
        fun show(any: Any) {
            ChildFragmentDialog().show(any)
        }
    }
}