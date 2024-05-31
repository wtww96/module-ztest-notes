package com.dp.notes.ui.statusbar

import android.os.Bundle
import androidx.core.view.WindowInsetsCompat.Type
import com.alibaba.android.arouter.facade.annotation.Route
import com.dp.common.route.PageRoute
import com.dp.core.base.BaseActivity
import com.dp.core.extension.clickEvent
import com.dp.core.extension.onBack
import com.dp.core.extension.scHeight
import com.dp.core.extension.screenSize
import com.dp.core.extension.showToast
import com.dp.core.viewbinding.bindings
import com.dp.core.windowinsets.fitStatusBar
import com.dp.core.windowinsets.fullScreen
import com.dp.core.windowinsets.hasNavigationBar
import com.dp.core.windowinsets.hideNavigation
import com.dp.core.windowinsets.hideStatus
import com.dp.core.windowinsets.immerse
import com.dp.core.windowinsets.navigationColor
import com.dp.core.windowinsets.navigationHeight
import com.dp.core.windowinsets.showNavigation
import com.dp.core.windowinsets.showStatus
import com.dp.core.windowinsets.statusColor
import com.dp.core.windowinsets.statusHeight
import com.dp.notes.R
import com.dp.notes.databinding.NotesActivityStatusbarBinding


/**
 * author Dq
 * date on 2022/11/17
 * description
 */
@Route(path = PageRoute.ACTIVITY_NOTES_STATUS)
class StatusbarActivity : BaseActivity(R.layout.notes_activity_statusbar) {
    private val binding by bindings<NotesActivityStatusbarBinding>()

    private var isFull = false//默认不是全屏

    override fun initView(bundle: Bundle?) {
        //状态栏沉浸式修复高度和添加无逻辑返回
        binding.btBack.fitStatusBar().onBack()

        binding.tvContent.text = "状态栏高度:${statusHeight}" +
                "\n底部导航栏高度:${navigationHeight}" +
                "\n手机屏幕可用高度,像素px:${scHeight}" +
                "\n手机屏幕真实高度,像素px:${screenSize.y}" +
                "\n是否有底部导航栏11:${hasNavigationBar}"
    }

    override fun initListener() {
        //系统栏沉浸式
        binding.bt1.clickEvent {
            immerse(Type.systemBars())
        }
        //仅状态栏沉浸式(其实还是全沉浸式,只不过给根布局设置paddingTop)
        binding.bt2.clickEvent {
            immerse(Type.statusBars())
        }
        //仅底部导航栏沉浸式(其实还是全沉浸式,只不过给根布局设置paddingBottom)
        binding.bt3.clickEvent {
            immerse(Type.navigationBars())
        }
        //全屏开关
        binding.bt4.clickEvent {
            isFull = !isFull
            fullScreen(isFull)
            binding.bt4.text = "全屏开关:${if (!isFull) "开" else "关"}"
        }
        //隐藏状态栏
        binding.bt5.clickEvent {
            hideStatus()
        }
        //显示状态栏
        binding.bt6.clickEvent {
            showStatus()
        }
        //隐藏底部导航栏
        binding.bt7.clickEvent {
            hideNavigation()
        }
        //显示底部导航栏
        binding.bt8.clickEvent {
            showNavigation()
        }
        //状态栏和底部导航栏高度
        binding.bt9.clickEvent {
            binding.tvContent.text = "状态栏高度:${statusHeight}" +
                    "\n底部导航栏高度:${navigationHeight}"
        }
        //修改状态栏和底部导航栏颜色
        binding.bt10.clickEvent {
            statusColor(R.color.colorPrimary)
            navigationColor("#6600ff00")
        }
        //是否有底部导航栏
        binding.bt11.clickEvent {
            binding.tvContent.text = "是否有底部导航栏 = $hasNavigationBar"
        }
    }

    override fun onPageBack() {
        //super.onPageBack()
        showToast("屏蔽系统返回")
    }
}