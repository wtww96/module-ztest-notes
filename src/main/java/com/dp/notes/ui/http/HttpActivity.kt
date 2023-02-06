package com.dp.notes.ui.http

import androidx.activity.viewModels
import com.dp.core.base.BaseActivity
import com.dp.core.extension.clickEvent
import com.dp.core.network.successIn
import com.dp.core.network.util.NetworkUtil
import com.dp.core.viewbinding.bindings
import com.dp.notes.R
import com.dp.notes.databinding.NotesActivityHttpBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * author Dq
 * date on 2022/11/1
 * description 封装的网络请求 使用示例
 */
@AndroidEntryPoint
class HttpActivity : BaseActivity(R.layout.notes_activity_http) {
    private val binding by bindings<NotesActivityHttpBinding>()
    private val viewModel by viewModels<HttpViewModel>()

    override fun initObserve() {
        NetworkUtil.getNetWorkSpeed(this, 1000) {
            binding.logText.add("当前网络速率 = $it")
        }
        //回调api 转 flow流
        viewModel.rxResult.observe(this) {
            binding.logText.add("回调api 转 flow流 livedata通知更新=$it")
        }

        //Flow流请求 LiveData通知更新
        viewModel.flowResult.observe(this) {
            binding.logText.add("Flow流请求,livedata通知更新=$it")
        }
    }

    override fun initListener() {
        //rxjava封装的网络请求--> 回调api 转 flow流
        binding.bt1.clickEvent {
            viewModel.rxRequest()
        }

        //Flow流请求,LiveData通知更新
        binding.bt2.clickEvent {
            viewModel.flowRequest1()
        }

        //Flow流请求,直接在页面处理更新
        binding.bt3.clickEvent {
            //处理 成功和失败 两种场景
            /*viewModel.flowRequest2().launchWith(this) {
                success {
                    binding.text.text = "${it.city} , ${it.realtime.info} , ${it.realtime.direct}"
                    Log.e("hehe", "flowRequest1_2 success{} ${it.toJson()}")
                }
                failure { e, _ ->
                    Log.e("hehe", "flowRequest1_2 failure{} = $e")
                }
            }*/

            //只关心成功结果,如需要显示loading,可直接在viewModel中处理
            viewModel.flowRequest2().successIn(this) {
                binding.logText.add("flow只处理success=${it.text}")
            }
            /*viewModel.flowRequest3().observe(this) {
                binding.logText.add("flow 转LiveData =${it}")
            }

            viewModel.flowRequest3().observe(this, this::xx)

            viewModel.flowRequest4().success(this) {
                binding.logText.add("flow 转LiveData 只处理success =${it.text}")
            }
            viewModel.flowRequest4().success(this, this::xx1)
            viewModel.flowRequest4().observe(this) {
                it.success {

                }
                it.failure { e, data ->

                }
            }*/
        }
    }

    /*fun xx(txt: String) {

    }

    fun xx1(bean: Test1Bean) {

    }*/
}
