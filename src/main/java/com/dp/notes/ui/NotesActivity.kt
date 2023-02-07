package com.dp.notes.ui

import android.content.Intent
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.dp.common.route.PageRoute
import com.dp.core.base.BaseActivity
import com.dp.core.event.FlowBus
import com.dp.core.extension.clickEvent
import com.dp.core.extension.navigateTo
import com.dp.core.extension.registerIntentResult
import com.dp.core.network.util.NetworkLiveData
import com.dp.core.network.util.NetworkUtil
import com.dp.core.viewbinding.bindings
import com.dp.core.windowinsets.ImeHelper
import com.dp.notes.R
import com.dp.notes.constants.EventKeys.KEY_TEST
import com.dp.notes.databinding.NotesActivityNotesBinding
import com.dp.notes.ui.dialog.UseDialogActivity
import com.dp.notes.ui.flow.FlowActivity
import com.dp.notes.ui.gson.GsonActivity
import com.dp.notes.ui.hilt.TestHiltActivity
import com.dp.notes.ui.http.HttpActivity
import com.dp.notes.ui.koin.TestKoinActivity
import com.dp.notes.ui.lazy.LazyActivity
import com.dp.notes.ui.mmkv.MmkvActivity
import com.dp.notes.ui.toast.ToastActivity
import com.dp.notes.ui.webview.WebViewUseActivity

/**
 * author Dq
 * date on 2022/11/11
 * description 主页
 */
@Route(path = PageRoute.ACTIVITY_NOTES_MAIN)
class NotesActivity : BaseActivity(R.layout.notes_activity_notes) {
    private val binding by bindings<NotesActivityNotesBinding>()
    private val launcher = registerIntentResult()

    override fun initView(bundle: Bundle?) {
        //网络速率监听
        NetworkUtil.getNetWorkSpeed(this) {
            binding.childView.tvTitle.text = "当前网络速率 = $it"
        }
        //网络连接状态监听
        NetworkLiveData.instance.observe(this) {
            binding.logText.add("网络连接状态监听 = $it")
        }
        //FlowBus 事件总线接受数据
        FlowBus.with<Int>(KEY_TEST).register(this) {
            binding.logText.add("主页已接收$KEY_TEST 发送的FlowBus数据=$it", true)
        }
        //FlowBus 事件总线,页面处于可见状态才接受数据
        FlowBus.with<Int>(KEY_TEST).registerWhenStarted(this) {
            binding.logText.add("页面处于可见状态 主页已接收$KEY_TEST 发送的FlowBus数据=$it")
        }
        //软键盘监听
        ImeHelper.addImeChangeCallback(this) {
            onStatus {
                binding.logText.add("软键盘-->${if (it) "显示" else "隐藏"}")
            }
        }

        //binding.etContent.focus(2000)

        //================================================

        /*lifecycleScope.tickFlow(10, {
            Log.e("hehe", "tickFlow = $it")
        }, {
            Log.e("hehe", "tickFlow finish")
        })
        lifecycleScope.delayed(3000) {
            Log.e("hehe", "delayed 3s")
        }
        lifecycleScope.poll(2000) {
            Log.e("hehe", "2s一次轮询")
        }
        lifecycleScope.pollWhenStarted(2000) {
            Log.e("hehe", "2s一次轮询 ,,,, pollWhenStarted")
        }*/
    }

    override fun initListener() {
        //hilt依赖注入
        binding.hilt.clickEvent {
            navigateTo<TestHiltActivity>()
        }

        //koin依赖注入
        binding.koin.clickEvent {
            navigateTo<TestKoinActivity>()
        }

        //registerResult使用
        binding.register.clickEvent {
            launcher.launch(Intent(this, TestRegisterResultActivity::class.java)) { result ->
                binding.logText.add(
                    "registerResult使用:\n" +
                        "     resultCode=${result.resultCode}\n" +
                        "     result=${result.data?.getStringExtra("backParams")}"
                )
            }
        }

        //Fragment懒加载
        binding.lazy.clickEvent {
            navigateTo<LazyActivity>()
        }

        //弹窗测试
        binding.dialog.clickEvent {
            navigateTo<UseDialogActivity>()
        }

        //Flow流使用和解析
        binding.flow.clickEvent {
            navigateTo<FlowActivity>()
        }

        //封装的网络库使用
        binding.http.clickEvent {
            navigateTo<HttpActivity>()
        }

        //Gson容错处理
        binding.gson.clickEvent {
            navigateTo<GsonActivity>()
        }

        //Mmkv封装使用
        binding.mmkv.clickEvent {
            navigateTo<MmkvActivity>()
        }

        //Toast使用
        binding.toast.clickEvent {
            navigateTo<ToastActivity>()
        }

        //WebView封装
        binding.webview.clickEvent {
            navigateTo<WebViewUseActivity>()
        }
    }
}