package com.dp.notes.ui.http

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dp.core.base.BaseViewModel
import com.dp.core.extension.toJson
import com.dp.core.network.failure
import com.dp.core.network.launchIn
import com.dp.core.network.success
import com.dp.notes.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject


/**
 * author Dq
 * date on 2022/11/1
 * description
 */
@HiltViewModel
class HttpViewModel @Inject constructor(private val dataSource: TestRepository) : BaseViewModel() {
    val rxResult = MutableLiveData<String>()
    val flowResult = MutableLiveData<String>()

    /**
     * rxjava封装的网络请求 转化 为flow流处理
     */
    fun rxRequest() {
        dataSource.requestRx().launchIn(viewModelScope) {
            success {
                rxResult.value = "${it.city} , ${it.realtime.info} , ${it.realtime.direct}"
                Log.e("hehe", "rxRequest 成功 = ${it.toJson()}")
            }
            failure { e, _ ->
                Log.e("hehe", "rxRequest 失败 = $e")
            }
        }
    }

    /**
     * flow流请求网络数据1.1 --> 接口返回的具体数据,创建一个flow流去处理数据结果和异常情况,返回
     * 如果数据比较复杂,可以在viewModel中处理数据源,再通过LivaData去更新ui,需定义
     * 需要在定义一个LiveData
     */
    fun flowRequest1() {
        dataSource.requestFlow().launchIn(viewModelScope) {
            success {
                flowResult.value = "${it.city} , ${it.realtime.info} , ${it.realtime.direct}"
                Log.e("hehe", "flowRequest1_1 成功 = ${it.toJson()}")
            }
            failure { e, _ ->
                Log.e("hehe", "flowRequest1_1 失败 = $e")
            }
        }
    }

    /**
     * flow流请求网络数据1.2 --> 请求模式同 flowRequest1_1
     * 处理数据是 直接返回flow流到Activity或Fragment,拿到数据直接更新
     *   1.只关心成功结果,并且 拿到数据直接显示,不需要额外处理数据
     *   2.如果需要加loading框 可以通过flow操作符处理:显示/隐藏loading
     *   3.Activity或Fragment不需要在增加任何额外代码
     *   注意:这里添加的onCompletion操作符不会收到异常信息,因为是在catch之后调用的
     *       只能当做请求完成来处理
     */
    fun flowRequest2() = dataSource.requestFlow()
        .onStart { showLoading() }
        .onCompletion { hideLoading() }
}