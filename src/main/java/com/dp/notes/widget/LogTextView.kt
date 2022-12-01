package com.dp.notes.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
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
            Toast.makeText(context, "已清空~~~", Toast.LENGTH_SHORT).show()
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
        if (isToast) Toast.makeText(context, str, Toast.LENGTH_LONG).show()
    }

    fun clear() {
        context
        sb.clear()
        setText(sb.toString())
    }

}