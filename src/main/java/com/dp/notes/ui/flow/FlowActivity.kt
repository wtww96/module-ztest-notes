package com.dp.notes.ui.flow

import android.view.View
import androidx.lifecycle.*
import com.dp.core.base.BaseActivity
import com.dp.core.event.FlowBus
import com.dp.core.extension.clickEvent
import com.dp.core.extension.delayed
import com.dp.core.viewbinding.bindings
import com.dp.notes.constants.EventKeys.KEY_TEST
import com.dp.notes.databinding.ActivityFlowBinding
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * author Dq
 * date on 2022/10/28
 * description
 */
class FlowActivity : BaseActivity() {
    private val binding by bindings<ActivityFlowBinding>()
    override fun getLayoutView(): View = binding.root

    /**
     * livedata:
     *           1.生命周期感知型组件: 生命周期处于活跃状态,才能更新数据
     *           2.粘性事件: 新的订阅者会收到1条最新的事件
     *           3.数据不防抖: 重复 setValue 相同的值，订阅者会收到多次 onChanged() 回调--可用过 distinctUntilChanged 扩展函数去重
     *           4.不支持背压: 当数据生产速度 > 数据消费速度时，中间就会有一部分数据被忽略
     *
     * flow冷流:
     *           1.不具备生命周期感知能力,运行在Activity的lifecycleScope协程作用域中,在Activity销毁会自动取消流
     *           2.流数据。冷流Flow/冷流callbackFlow 和 热流SharedFlow/StateFlow
     *                    冷流: 订阅者发起订阅(collect)，事件才会开始发送消息，发送完后就会自动回收,不缓存消息
     *                    热流: 无论是否有订阅者，都可以生产数据并且缓存在内存中(新订阅者订阅后能否收到数据与粘性数据有关)
     *           3.需要订阅收集(collect操作符),才能发送数据
     *           4.每次订阅都会创建一个全新的数据流,重新发送
     *           5.shareIn/stateIn(scope, Eagerly)-->冷流 转为 热流
     *             SharingStarted.Eagerly(热启动式): 立即启动数据流，并保持数据流(直到scope指定的作用域结束)
     *             SharingStarted.Lazily(懒启动式): 在首个订阅者注册时启动，并保持数据流(直到scope指定的作用域结束)
     *
     * sharedFlow热流(不具备生命周期感知能力):
     *            1.默认情况,没有粘性事件
     *            2.没有默认值
     *            3.数据不防抖
     *            使用场景:
     *                     1.一次性事件,不需要重放,比如toast,弹窗等ui事件
     *                     2.封装为事件总线FlowBus
     *                     3.侧重在事件:更适合通知ui界面的一些事件，比如toast等
     *
     * stateFlow热流(不具备生命周期感知能力):
     *            是一个特殊的SharedFlow = MutableSharedFlow(replay = 0,onBufferOverflow = BufferOverflow.DROP_OLDEST)
     *            1.粘性事件，新的订阅者会获得当前的最新事件
     *            2.有初始默认值，并且在开始订阅时会回调初始值
     *            3.数据防抖
     *            4.最接近LiveData的Flow
     *            使用场景:
     *                     1.可以替代ViewModel中LiveData传递数据
     *                     2.侧重在状态:因为永远有最新的值，可以让页面UI处于一个最新状态，会丢失中间或者过期的状态，保持最新状态，
     *                       可用于页面ui数据状态更新
     *
     * callbackFlow冷流:
     *            1.没有接收者，不会产生数据。
     *            2.将基于回调的Api转化为数据流
     *            3.callbackFlow创建的流不会主动关闭，需手动调用close() 或者 外部协程被取消(外部协程绑定生命周期)
     *            4.调用close() 或者协程被取消 会触发awaitClose{}代码块，可执行释放资源/取消网络请求等相关操作
     *
     * ************************************************************************************************************************************
     *
     * repeatOnLifecycle:
     *            1.绑定生命周期,可以配合flow流使用,让flow具备感知生命周期的能力
     *            2.每当生命周期>=目标状态时,会在一个新的协程中执行传入的代码块(每次走到这个生命周期都会触发执行,并不是首次才会触发)
     *            3.每当生命周期<=目标状态时,执行传入代码块而启动的协程会被取消,(同上~每次都会执行)
     *            4.生命周期处于DESTROYED状态时,切换回调用repeatOnLifecycle的协程,继续执行
     *            5.flowWithLifecycle 是Flow流扩展函数,感知生命周期
     *            6.注意:
     *                  如果是Flow流使用flowWithLifecycle扩展函数,需要注意的问题:
     *                  发送数据流的代码(emit/trySend)是否被传入到了repeatOnLifecycle中,
     *                  如果传入了,每当生命周期>=目标状态都会重新走发送数据流的代码
     *
     *            val flow = MutableSharedFlow<Int>()
     *            //repeatOnLifecycle是一个挂起函数,因此从lifecycleScope中创建新的协程
     *            lifecycleScope.launch {
     *                //直到 lifecycle 进入 DESTROYED 状态前都将当前协程挂起
     *                //每当生命周期处于 STARTED 或以后的状态时会在新的协程中启动执行代码块
     *                //并在生命周期进入 STOPPED 时取消协程
     *                repeatOnLifecycle(Lifecycle.State.STARTED) {
     *                    //当生命周期处于 STARTED 时安全地收集数据
     *                    //当生命周期进入 STOPPED 时停止收集数据
     *                    flow.collect {
     *                        //更新...
     *                    }
     *                }
     *                //注意：运行到此处时，生命周期已经处于 DESTROYED 状态！
     *            }
     */
    override fun initView() {
        //livedata
        testLiveData()
        //Flow 冷流
        testFlow()
        //SharedFlow 热流
        testSharedFlow()
        //StateFlow 热流
        testStateFlow()
        //Flow不具备感知生命周期,所有需要绑定页面生命周期,防止内存泄漏或奔溃
        testFlowLifecycle()
        //FlowBus
        testFlowBus()
        //FlowBus 事件总线接受数据
        FlowBus.with<Int>(KEY_TEST).register(this) {
            binding.logText.add("FlowActivity:FlowBus接受数据=$it")
        }
    }

    /**
     * livedata
     */
    private fun testLiveData() {
        val livedata = MutableLiveData<Int>()
        livedata.observe(this, Observer {
            binding.logText.add("livedata 订阅,接受数据=$it")
        })
        binding.livedata.clickEvent {
            lifecycleScope.launch {
                (1..5).forEach {
                    binding.logText.add("livedata,发送数据=$it")
                    livedata.value = it
                    delay(1000)
                }
            }
        }
    }

    /**
     * Flow冷流
     */
    private fun testFlow() {
        val flowData = flow {
            (1..5).forEach {
                delay(1000)
                binding.logText.add("flow冷流,发送数据=$it")
                emit(it)
            }
        }
        //.shareIn(lifecycleScope, SharingStarted.Eagerly)
        //.stateIn(lifecycleScope, SharingStarted.Eagerly, 0)

        //---------------
        binding.flow1.clickEvent {
            lifecycleScope.launch {
                flowData.collect {
                    binding.logText.add("flow冷流1,接受数据=$it")
                }
            }
        }
        binding.flow2.clickEvent {
            lifecycleScope.launch {
                flowData.collect {
                    binding.logText.add("flow冷流2,接受数据=$it")
                }
            }
        }
    }

    /**
     * SharedFlow热流
     */
    private fun testSharedFlow() {
        val flowData = MutableSharedFlow<Int>(
            //事件粘滞数:当新订阅者注册后,能够收到缓存的replay个最新的事件
            //reply=1,有点类似Livedata
            replay = 0,
            //额外缓存容量,接受的慢时候，发送的入栈
            extraBufferCapacity = 2,
            //缓存溢出策略
            //SUSPEND     : 挂起
            //DROP_OLDEST : 丢弃最早的一个
            //DROP_LATEST : 丢弃最近的一个
            onBufferOverflow = BufferOverflow.SUSPEND
        )

        //---------------

        //SharedFlow不去重
        /*lifecycleScope.launch {
            flowData.collect {
                binding.logText.add("SharedFlow热流,接受数据=$it")
            }
        }
        binding.sharedFlow.clickEvent {
            lifecycleScope.launch {
                val random = Random.nextInt(5)
                binding.logText.add("SharedFlow热流,发送数据=$random")
                flowData.emit(random)
            }
        }*/

        //---------------

        binding.sharedFlow.clickEvent {
            lifecycleScope.launch {
                launch {
                    flowData.collect {
                        binding.logText.add("SharedFlow热流,发送流之 前 ,订阅接受数据=$it")
                    }
                }
                launch {
                    (1..5).forEach {
                        binding.logText.add("SharedFlow热流,开始发送流=$it")
                        flowData.emit(it)
                    }
                }
                delay(2000)
                //延迟2s后:新的订阅者注册,replay表示:会重新发送 replay 条最新的数据给新订阅者
                //replay=0 新的订阅者是收不到旧数据中的最新数据的
                launch {
                    flowData.collect {
                        binding.logText.add("SharedFlow热流,发送流之 后 ,订阅接受数据=$it")
                    }
                }
            }
        }
    }

    /**
     * StateFlow热流
     */
    private fun testStateFlow() {
        val flowData = MutableStateFlow(-1)

        //---------------

        //会最先接受到初始默认值,数据防抖
        /*lifecycleScope.launch {
            flowData.collect {
                binding.logText.add("StateFlow热流,接受数据=$it")
            }
        }
        binding.sharedFlow.clickEvent {
            lifecycleScope.launch {
                val random = Random.nextInt(5)
                binding.logText.add("StateFlow热流,发送数据=$$random")
                flowData.emit(random)
            }
        }*/
        //有初始默认值，并且在开始订阅时会回调初始值
        //粘性事件，新的订阅者会获得当前的最新事件(会丢掉中间事件)
        //---------------
        lifecycleScope.launch {
            flowData.collect {
                binding.logText.add("StateFlow热流,外部,订阅接受数据=$it")
            }
        }
        binding.stateFlow.clickEvent {
            lifecycleScope.launch {
                (1..5).forEach {
                    binding.logText.add("StateFlow热流,开始发送流=$it")
                    flowData.emit(it)
                    //不延迟的话:新订阅者只会收到最新一条数据
                    //延迟的话:会正常接收每条数据
                    //delay(1000)
                }
            }
            lifecycleScope.launch {
                flowData.collect {
                    binding.logText.add("StateFlow热流,发送流后立马,订阅接受数据=$it")
                }
            }
            lifecycleScope.launch {
                delay(2000)
                binding.logText.add("delay 2s后:")
                flowData.collect {
                    binding.logText.add("StateFlow热流,新订阅接受最新一条数据=$it")
                }
            }
        }
    }

    /**
     * Flow不具备感知生命周期,所有需要绑定页面生命周期,防止内存泄漏或奔溃
     */
    private fun testFlowLifecycle() {
        //每次onstart后都会触发:发送数据
        binding.flowLifecycle1.clickEvent {
            //如果是Flow流使用flowWithLifecycle扩展函数,需要注意一点:
            //发送数据流的代码是否被传入到了repeatOnLifecycle中,如果传入了,每当生命周期>=目标状态都会重新发送数据流
            //如下代码就是如此....
            lifecycleScope.launch {
                callbackFlow {
                    binding.logText.add("callbackFlow,flowWithLifecycle delay 2s")
                    delay(2000)
                    val data = Random.nextInt(999)
                    binding.logText.add("callbackFlow,flowWithLifecycle 发送 数据=$data")
                    trySend(data)
                    awaitClose {}
                }.flowWithLifecycle(lifecycle).collect {
                    binding.logText.add("callbackFlow,flowWithLifecycle 接收 数据=$it")
                }
            }
        }
        //--------------------
        val flow = MutableSharedFlow<Int>()
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collect {
                    binding.logText.add("sharedFlow,repeatOnLifecycle 接收并send 数据=$it")
                }
            }
        }
        binding.flowLifecycle2.clickEvent {
            lifecycleScope.launch {
                binding.logText.add("sharedFlow,emit delay 2s")
                delay(4000)
                val data = Random.nextInt(666)
                binding.logText.add("sharedFlow,emit 发送 数据=$data")
                flow.emit(data)
            }
        }
    }

    /**
     * sharedFlow封装事件总线
     */
    private fun testFlowBus() {
        binding.flowBus.clickEvent {
            val random1 = Random.nextInt(999)
            binding.logText.add("FlowBus 发送数据1=$random1")
            FlowBus.with<Int>(KEY_TEST).post(this, random1)
            lifecycleScope.delayed(2000) {
                val random2 = Random.nextInt(999)
                binding.logText.add("FlowBus 发送数据22=$random2")
                FlowBus.with<Int>(KEY_TEST).post(this, random2)
            }
        }
    }
}