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

    protected class TaskBuilder {
        var showLoading: Boolean = true
        var catch: ((Exception) -> Unit)? = null

        fun showLoading(showLoading: Boolean): TaskBuilder {
            this.showLoading = showLoading
            return this
        }

        fun catch(catch: ((Exception) -> Unit)? = null): TaskBuilder {
            this.catch = catch
            return this
        }
    }

    protected fun runTask(showLoading: Boolean = true) = TaskBuilder().showLoading(showLoading)

    protected inline fun runTask(showLoading: Boolean = true, crossinline action: suspend CoroutineScope.() -> Unit) =
        TaskBuilder().showLoading(showLoading).action(action)

    protected inline fun TaskBuilder.action(crossinline action: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (showLoading) {
                    showLoad.postValue(true)
                    //测试用
                    delay(1000)
                }

                action()

            } catch (e: Exception) {
                if (catch != null) {
                    catch?.invoke(e)

                } else {
                    showError(e)
                }

            } finally {
                if (showLoading) {
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