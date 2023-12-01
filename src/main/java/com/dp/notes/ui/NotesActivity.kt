package com.dp.notes.ui

import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.dp.common.route.PageRoute
import com.dp.core.base.BaseActivity
import com.dp.core.constants.PermissionConst
import com.dp.core.event.FlowBus
import com.dp.core.extension.*
import com.dp.core.network.util.NetworkLiveData
import com.dp.core.network.util.NetworkUtil
import com.dp.core.viewbinding.bindings
import com.dp.core.windowinsets.ImeHelper
import com.dp.notes.R
import com.dp.notes._temp.screenshot.ScreenImageManager
import com.dp.notes._temp.screenshot.ScreenShotManager
import com.dp.notes._temp.screenshot.ScreenVideoManager
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
import com.dp.notes.ui.state.StateActivity
import com.dp.notes.ui.tablayout.TabLayoutActivity
import com.dp.notes.ui.tablayout.TabLayoutNewActivity
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
    private val launcherPermission = registerPermissionResult()

    override fun initView(bundle: Bundle?) {
        //监听截屏/录屏 没有出来销毁逻辑,有内存泄漏
        //registerScreenshot()

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

        //registerResult,页面跳转的使用
        binding.register1.clickEvent {
            launcher.launch(intentTo<TestRegisterResultActivity>()) { result ->
                binding.logText.add(
                    "registerResult使用:\n" +
                        "     resultCode=${result.resultCode}\n" +
                        "     result=${result.data?.getStringExtra("backParams")}"
                )
            }
        }

        //registerResult,申请权限的使用
        binding.register2.clickEvent {
            launcherPermission.launchP(PermissionConst.STORAGE) { isAllow, asks ->
                binding.logText.add("存储权限申请结果 = $isAllow")
                asks.forEach {
                    binding.logText.add("拒绝且不再询问的权限组 = $it")
                }
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

        //tabLayout
        binding.tabLayout.clickEvent {
            navigateTo<TabLayoutActivity>()

        }
        binding.tabLayout.setOnLongClickListener {
            navigateTo<TabLayoutNewActivity>()
            true
        }

        //页面状态管理
        binding.state.clickEvent {
            navigateTo<StateActivity>()
        }
    }

    private fun registerScreenshot() {
        ScreenShotManager.instance.registerListener(
            {   //处理查询到的最新图片/视频 数据
                Log.e("hehe", "App 收到要处理的文件数据 =$it")
                if (it.isImage) {
                    ScreenImageManager.instance.handleImage(it)
                } else {
                    ScreenVideoManager.instance.handleVideo(it)
                }
            },
            {
                Log.e("hehe", "收到要申请存储权限的请求,uri=$it")
                launcherPermission.launchP(PermissionConst.STORAGE) { isAllow, asks ->
                    Log.e("hehe", "存储权限回调结果,isAllow = $isAllow")
                    //收到存储权限回调,处理数据
                    ScreenShotManager.instance.handleMediaContentChange(this, it, "dfsdf", "洒洒水所所所")
                }
            }
        )
    }
}