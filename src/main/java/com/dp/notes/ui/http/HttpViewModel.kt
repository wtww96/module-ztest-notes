package com.dp.notes.ui.http

import android.util.Log
import androidx.lifecycle.*
import com.dp.core.base.BaseViewModel
import com.dp.core.extension.toJson
import com.dp.core.network.*
import com.dp.core.network.bean.NetworkResult
import com.dp.notes.bean.Test1Bean
import com.dp.notes.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.measureTimeMillis


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
        dataSource.requestRx().wrap(this) {
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
        dataSource.requestFlow().asLiveData()
        dataSource.requestFlow().wrap(this) {
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

    fun flowRequest3() = liveData {
        dataSource.requestFlow().collect {
            it.success {
                emit("liveData{} --> success = ${it.city} , ${it.realtime.info} , ${it.realtime.direct}")
            }
            it.failure { e, _ ->
                emit("liveData{} -->  failure = $e")
            }
        }
    }

    fun flowRequest4(): LiveData<NetworkResult<Test1Bean>> = dataSource.requestFlow().asLiveData()

    fun flowRequest5(): LiveData<NetworkResult<Test1Bean>> = dataSource.requestFlow()
        .onStart { showLoading() }
        .onCompletion { hideLoading() }
        .asLiveData()

    /**
     * zip 操作符 合并多个请求结果
     * 多个网络请求是 并行执行 的
     */
    fun flowRequestZip() {
        //多个接口拿到的都是Flow<NetworkResult<T>>数据格式,内部包了一层NetworkResult,
        //很多请求下合并多个请求结果是不需要关注失败的情况,所以只需要拿到成功结果合并即可

        //写法1:
        /*dataSource.requestRx(" 第一 ")
            .zip(dataSource.requestFlow()) { data1, data2 ->
                val value1 = if (data1 is SuccessResult) {
                    data1.data.text
                } else if (data1 is FailedResult) {
                    data1.throwable.message ?: "Failed"
                } else {
                    "data1 null"
                }

                val value2 = if (data2 is SuccessResult) {
                    data2.data.text
                } else if (data2 is FailedResult) {
                    data2.throwable.message ?: "Failed"
                } else {
                    "data2 null"
                }
                value1.plus("  -----  " + value2)
            }
            .launchIn(this) {
                Log.e("hehe", "zip launchIn =$it")
            }*/

        //写法2:添加 NetworkResult<T>.asData 扩展函数,直接拿到成功的结果,失败就为null
        dataSource.requestRx(" 第一 ")
            .zip(dataSource.requestFlow()) { data1, data2 ->
                val value1 = data1.asData()?.text
                val value2 = data2.asData()?.text ?: "data2 失败"
                value1.plus("  -----  " + value2)
            }
            .launchIn(this) {
                Log.e("hehe", "zip launchIn =$it")
            }
    }

    /**
     * 用 async await 处理网络请求,可用于一个请求需要拿到另一个请求的结果作为参数去调用的场景
     * async:开启一个带Deferred<T> 返回值的协程作用域(注意:不是挂起函数)
     * await:用于获取async协程作用域返回的Deferred<T>中T的值(注意:是挂起函数,要等await拿到返回值后才会走后面的代码,要注意await的调用时机)
     *
     * async { funtion() }.await() / async { funtion() }
     * 不管有不有.await(),都会先触发async中的代码块
     * .await()是用来等async中的代码块执行完后获取其结果
     * { }后面不调用.await(),也同样会触发代码块
     *
     * eg:下面三个例子
     */
    fun flowRequestAsync() {
        viewModelScope.launch {
            Log.e("hehe", "launch init")
            val time = measureTimeMillis {
                Log.e("hehe", "measureTimeMillis init")
                //await直接跟在async{ }后面,会先触发{ }中的代码块,直到执行完成await拿到返回值才会走后续代码
                //串行触发  总耗时3s
                val value1 = async { doSomethingUsefulOne() }.await()
                Log.e("hehe", "value111 = $value1")
                val value2 = async { doSomethingUsefulTwo() }.await()
                Log.e("hehe", "value222 = $value2")

                //调用async{ },再接着调用第二个async{ },然后再去一个一个调用await
                //两个async中的代码块都是并行触发  总耗时2s
                /*val async1 = async { doSomethingUsefulOne() }
                Log.e("hehe", "async111 触发完毕-----")
                val async2 = async { doSomethingUsefulTwo() }
                Log.e("hehe", "async222 触发完毕-----")
                val value2 = async2.await()
                Log.e("hehe", "value222 = $value2")
                val value1 = async1.await()
                Log.e("hehe", "value111 = $value1")*/

                //--------------------------
                /*val async1 = async { doSomethingUsefulOne() }
                Log.e("hehe", "async111 触发完毕-----")
                val value1 = async1.await()
                Log.e("hehe", "value111 = $value1")

                val async2 = async { doSomethingUsefulTwo() }
                Log.e("hehe", "async222 触发完毕-----")
                val value2 = async2.await()
                Log.e("hehe", "value222 = $value2")*/
                //Log.e("hehe", "合并结果 = ${value1 + value2}")
            }
            Log.e("hehe", "Completed time = $time")
        }
    }

    private suspend fun doSomethingUsefulOne(): Int {
        Log.e("hehe", "doSomethingUsefulOne 11111")
        delay(1000L)
        Log.e("hehe", "doSomethingUsefulOne 11111---------")
        return 666
    }

    private suspend fun doSomethingUsefulTwo(): Int {
        Log.e("hehe", "doSomethingUsefulTwo 222222")
        delay(2000L)
        Log.e("hehe", "doSomethingUsefulOne 222222---------")
        return 888
    }
}