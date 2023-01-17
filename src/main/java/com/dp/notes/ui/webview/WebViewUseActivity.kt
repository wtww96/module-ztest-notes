package com.dp.notes.ui.webview

import com.dp.common.PageRouteAction
import com.dp.core.base.BaseActivity
import com.dp.core.extension.clickEvent
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesActivityWebviewUseBinding
import com.dp.webview.bean.WebParamBean

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
            PageRouteAction.launchWebView("http://www.baidu.com")
        }

        //传递Bean对象 WebView Activity
        binding.bt2.clickEvent {
            PageRouteAction.launchWebView(WebParamBean("http://www.cxy521.com/", statusBarDarkText = false))
        }
    }
}