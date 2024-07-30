package com.dp.notes.repository

import com.dp.core.network.FlowNet
import com.dp.core.network.NetworkManager
import com.dp.core.network.bean.NetworkResult
import com.dp.core.network.flowRequest
import com.dp.core.network.rxjava.flowRxRequest
import com.dp.notes.bean.Test1Bean
import com.dp.notes.network.ApiService
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * author Dq
 * date on 2022/9/15
 * description  Repository里面返回的数据都是处理好了的Flow<NetworkResult<T>>数据
 *              方便viewModel使用,也可以在viewModel中直接返回Flow流到Activity/Fragment通知页面更新
 *              这样可以少定义一个LiveData去通知页面更新
 */
class TestRepositoryImpl @Inject constructor() : TestRepository {
    private val api by lazy { NetworkManager.instance.getApi<ApiService>() }

    override fun requestRx(params: String): FlowNet<Test1Bean> {
        return flowRxRequest(api.queryWeatherRx())
    }

    override fun requestFlow(): FlowNet<Test1Bean> {
        return flowRequest { api.queryWeatherFlow() }
    }

    override fun requestFlowList(): FlowNet<List<Test1Bean>> {
        return flowOf(NetworkResult.SuccessResult(emptyList()))
    }
}