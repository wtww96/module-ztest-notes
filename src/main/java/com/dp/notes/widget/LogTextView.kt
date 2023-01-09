package com.dp.notes.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView
import com.dp.core.extension.showToast
import com.dp.core.windowinsets.imeVisible


/**
 * author Dq
 * date on 2022/11/11
 * description
 */
class LogTextView(context: Context, attrs: AttributeSet?) : AppCompatTextView(context, attrs) {

    private val sb = StringBuilder()

    init {
        setOnLongClickListener {
            clear()
            showToast("已清空~~~")
            false
        }

        setOnClickListener {
            this.context
            Log.e("hehe", "View ime isVisible = ${imeVisible()}")
        }
    }

    fun add(str: String, isToast: Boolean = false) {
        sb.append(str.plus("\n"))
        setText(sb.toString())
        Log.e("TestLog", str)
        if (isToast) showToast(str, true)
    }

    fun clear() {
        context
        sb.clear()
        setText(sb.toString())
    }

}