package com.dp.notes.ui.koin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dp.core.base.BaseViewModel
import com.dp.core.network.failure
import com.dp.core.network.wrap
import com.dp.core.network.success
import com.dp.notes.repository.TestRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * author Dq
 * date on 2022/9/15
 * description
 */
class TestKoinViewModel(private val dataSource: TestRepository) : BaseViewModel() {
    val result = MutableLiveData<String>()
    val resultState = MutableStateFlow("StateFlow默认值")
    val resultShared = MutableSharedFlow<String>()

    fun getRequest() {
        showLoading()
        dataSource.requestRx().wrap(this) {
            success {
                result.value = it.text
                hideLoading()
            }
            failure { e, data ->
                hideLoading()
            }
        }
    }

    fun requestNew() = dataSource.requestFlow()

    fun changeState(str: String) {
        result.value = "LiveData = $str"
        resultState.value = "StateFlow = $str"
        viewModelScope.launch {
            resultShared.emit("SharedFlow,emit = $str")
        }
    }
}