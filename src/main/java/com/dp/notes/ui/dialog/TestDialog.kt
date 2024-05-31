package com.dp.notes.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.WindowManager.LayoutParams
import com.dp.notes.R


/**
 * author Dq
 * date on 2023/11/1
 * description
 */
class TestDialog(context: Context) : Dialog(context, R.style.Theme_Dialog_Base) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置Dialog的宽度和高度
        // 设置Dialog的宽度和高度
        val params = window!!.attributes
        params.width = LayoutParams.WRAP_CONTENT
        params.height = LayoutParams.WRAP_CONTENT
        // 将Dialog的位置设置到屏幕外面
        // 将Dialog的位置设置到屏幕外面
        params.gravity = Gravity.TOP or Gravity.START
        params.x = -2000
        params.y = -2000
        window!!.attributes = params

        setContentView(R.layout.notes_dialog_test)
    }

    override fun show() {
        kotlin.runCatching { super.show() }.onFailure {
            it.printStackTrace()
            Log.e("hehe", "show Catching=$it")
        }
    }
}