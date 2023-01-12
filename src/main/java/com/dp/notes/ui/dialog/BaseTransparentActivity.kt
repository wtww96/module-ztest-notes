package com.dp.notes.ui.dialog

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.dp.core.base.BaseActivity
import com.dp.notes.R

/**
 * author Dq
 * date on 2023/1/11
 * description 背景透明Activty,特定场景可用于代替Dialog,需要在Manifest中设置透明主题
 */
abstract class BaseTransparentActivity(@LayoutRes layoutId: Int) : BaseActivity(layoutId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTranslucent()
    }

    private fun initTranslucent() {
        //当前布局如果是显示在底部,设置底部显示动画
        val curGravity = (rootView.layoutParams as? FrameLayout.LayoutParams)?.gravity
        //底部显示,显示动画默认没变,设置指定底部动画
//        if (curGravity == Gravity.BOTTOM && anim() == R.style.AnimAlpha) {
//            Log.e("hehe", "AnimBottom")
//            window.setWindowAnimations(R.style.AnimBottom)
//        } else {
//            Log.e("hehe", "AnimAlpha")
//            window.setWindowAnimations(anim())
//        }
        //设置背景暗色,默认主题为0.4,可通过重写手动修改
        if (-1f != dimAmount()) window.setDimAmount(dimAmount())
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        overridePendingTransition(R.anim.anim_bottom_in, R.anim.anim_none)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_none, R.anim.anim_bottom_out)
    }

    protected open fun anim(): Int = R.style.AnimAlpha//透明Activity显示动画
    protected open fun dimAmount(): Float = -1f//背景暗色,默认为主题设置的0.4,可通过重写手动修改
}