package com.dp.notes.ui.hilt

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dp.core.base.BaseViewModel
import com.dp.core.extension.toJson
import com.dp.core.network.failure
import com.dp.core.network.launchWithIn
import com.dp.core.network.success
import com.dp.notes.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * author Dq
 * date on 2022/9/15
 * description
 */
@HiltViewModel
class TestHiltViewModel @Inject constructor(val dataSource: TestRepository) : BaseViewModel() {
    val rxResult = MutableLiveData<String>()
    val flowResult = MutableLiveData<String>()

    /**
     * rxjava封装的网络请求 转化 为flow流处理
     */
    fun rxRequest() {
        dataSource.requestRx().launchWithIn(this) {
            success {
                Log.e("hehe", "rxRequest success ${it.text}")
                rxResult.value = "${it.text}"
            }
            failure { e, _ ->
                Log.e("hehe", "rxRequest 失败 = ${e.toJson()}")
            }
        }
    }

    /**
     * flow流请求网络数据1.1 --> 接口返回的具体数据,在创建一个flow流,处理数据结果和异常情况
     * 如果数据比较复杂,可以在viewModel中处理数据源,再通过LivaData去更新ui
     * 需要在定义一个LiveData
     */
    fun flowRequest1() {
        dataSource.requestFlow().launchWithIn(this) {
            success {
                val data = "${it.city} , ${it.realtime.info} , ${it.realtime.direct}"
            }
            failure { e, _ ->
                Log.e("hehe", "flowRequest1 失败 = ${e.toJson()}")
            }
        }
    }

    /**
     * flow流请求网络数据1.2 --> 接口返回的具体数据,在创建一个flow流,处理数据结果和异常情况
     * 如果数据源比较简单,可以直接返回flow数据流,Activity或Fragment拿到数据直接更新
     * 不需要再去定义一个LiveData了
     */
    fun flowRequest2() = dataSource.requestFlow()
}