package com.base.common.base.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.base.common.util.log
import com.base.common.util.showError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 这里高阶函数函数运行必须使用 action() 方式，使用 action.invoke() 会报错
 */
private const val TAG = "BaseMVVMViewModel"

abstract class BaseMVVMViewModel : ViewModel() {
    val showLoad by lazy { MutableLiveData<Boolean>() }

    inline fun runTask(
        isShowDialog: Boolean = true,
        crossinline action: suspend CoroutineScope.() -> Unit,
        noinline error: ((Exception) -> Unit)? = null
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (isShowDialog) {
                showLoad.postValue(true)
                //测试用
                delay(1000)
            }

            action()

        } catch (e: Exception) {
            if (error != null) {
                error(e)
            } else {
                showError(e)
            }
        } finally {
            if (isShowDialog) {
                log("BaseMVVMViewModel", "runTaskDialog finally")
                showLoad.postValue(false)
            }
        }
    }

    override fun onCleared() {
        log(TAG, "onCleared")
        super.onCleared()
    }
}