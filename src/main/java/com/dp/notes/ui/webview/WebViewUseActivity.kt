package com.dp.notes.ui.webview

import android.util.Log
import com.dp.core.base.BaseActivity
import com.dp.core.extension.clickEvent
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesActivityWebviewUseBinding
import com.dp.webview.bean.WebParamBean
import com.dp.webview.PageWebAction

/**
 * author Dq
 * date on 2023/1/10
 * description WebView封装使用
 */
class WebViewUseActivity : BaseActivity(R.layout.notes_activity_webview_use) {
    private val binding by bindings<NotesActivityWebviewUseBinding>()

    override fun initListener() {
        //普通WebView Activity
        binding.bt1.clickEvent {
            PageWebAction.launch("http://www.baidu.com")
        }

        //传递Bean对象 WebView Activity
        binding.bt2.clickEvent {
            PageWebAction.launch(WebParamBean("http://www.cxy521.com/", statusBarDarkText = false))
        }

        //获取 WebViewFragment,自定义界面
        binding.bt3.clickEvent {
            val fragment = PageWebAction.newWebInstance(WebParamBean("http://www.cxy521.com/"))
            Log.e("hehe", "fragment = $fragment")
        }
    }
}