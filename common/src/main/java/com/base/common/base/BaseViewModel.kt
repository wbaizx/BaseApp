package com.base.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.base.common.helper.createStickStateFlow
import com.base.common.helper.safeLaunch
import com.base.common.helper.withThreadPoolContext
import com.base.common.util.debugLog
import com.base.common.util.showError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

const val BASE_VIEW_MODEL_TAG = "BASE_VIEW_MODEL_TAG"

abstract class BaseViewModel : ViewModel() {
    val showLoad by lazy { createStickStateFlow<Boolean>() }

    protected inline fun runTask(
        showLoading: Boolean = true,
        noinline catch: ((Throwable) -> Unit)? = null,
        crossinline block: suspend CoroutineScope.() -> Unit
    ) =
        viewModelScope.safeLaunch(Dispatchers.IO, catch = {
            if (catch != null) {
                catch(it)

            } else {
                showError(it)
            }

        }) {
            try {
                if (showLoading) {
                    showLoad.emit(true)
                }

                //测试
                withContext(Dispatchers.Default) {
                    debugLog(BASE_VIEW_MODEL_TAG, "test withContext")
                    delay(500)
                }

                //测试
                withThreadPoolContext {
                    Thread.sleep(500)
                    debugLog(BASE_VIEW_MODEL_TAG, "test withThreadPoolContext")
                }

                block()

            } finally {
                if (showLoading) {
                    debugLog(BASE_VIEW_MODEL_TAG, "runTask finally")
                    showLoad.emit(false)
                }
            }
        }


    override fun onCleared() {
        debugLog(BASE_VIEW_MODEL_TAG, "onCleared")
        super.onCleared()
    }
}