package com.base.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.base.common.util.NoParMutableStateFlow
import com.base.common.util.log
import com.base.common.util.showError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "BaseViewModel"

abstract class BaseViewModel : ViewModel() {
    val showLoad by lazy { NoParMutableStateFlow<Boolean>() }

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
                    showLoad.emit(true)
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
                    showLoad.emit(false)
                }
            }
        }

    override fun onCleared() {
        log(TAG, "onCleared")
        super.onCleared()
    }
}