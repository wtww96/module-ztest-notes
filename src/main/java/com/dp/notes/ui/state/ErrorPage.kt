package com.dp.notes.ui.state

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewParent
import androidx.appcompat.widget.AppCompatTextView
import com.dp.core.loadsir.PageState

/**
 * author Dq
 * date on 2024/2/20
 * description 错误状态页,通过反射去缓存
 */
class ErrorPage : PageState() {

    override fun createView(context: Context): View = AppCompatTextView(context).apply {
        Log.e("hehe", "ErrorPage  >>>>>>>>>>>>>>>>>>>>>> createView ")
        text = "我是错误的页面"
        textSize = 30f
        setBackgroundColor(Color.parseColor("#ff0000"))
        //layoutParams = FrameLayout.LayoutParams(100.dp, 100.dp).apply { gravity = Gravity.CENTER }
    }

    override fun onAttach(parent: ViewParent?) {
        Log.e("hehe", "333333333333333-->Error*Page onAttach $this , view=$view")
    }

    override fun onDetach(parent: ViewParent?) {
        Log.e("hehe", "333333333333333-->Error*Page onDetach $this , view=$view")
    }
}