package com.base.common.helper

import com.base.common.util.debugLog
import kotlinx.coroutines.*
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

const val COROUTINE_HELPER_TAG = "COROUTINE_HELPER_TAG"

/**
 * 安全启动协程，并做统一异常处理
 */
inline fun CoroutineScope.safeLaunch(
    context: CoroutineContext = Dispatchers.Main,
    crossinline catch: ((Throwable) -> Unit) = {},
    crossinline block: suspend CoroutineScope.() -> Unit
) =
    launch(context = context + CoroutineExceptionHandler { _, e ->
        //catch回调在启动协程的根context中，例如这里默认Dispatchers.Main，那么catch回调在主线程
        debugLog(COROUTINE_HELPER_TAG, "safeLaunch coroutineExceptionHandler $e")
        catch(e)

    }) {
        try {
            debugLog(COROUTINE_HELPER_TAG, "safeLaunch start")
            block()

        } catch (e: Exception) {
            if (e is CancellationException) {
                debugLog(COROUTINE_HELPER_TAG, "safeLaunch is cancel")

            } else {
                throw e
            }
        } finally {
            debugLog(COROUTINE_HELPER_TAG, "safeLaunch end")
        }
    }


/**
 * 模拟 withContext 方法，主要用于必须使用自己的线程池，而又要保留协程用法的情况
 * 使用此方法如果要完全规避使用协程内部的线程池，launch方法必须使用 Dispatchers.Main，确保协程内部调度只用到主线程
 *
 * 或者使用自定义Dispatcher，但这种方法如果线程池中做了特殊处理就不好处理，比如线程监控逻辑
 */
suspend inline fun <T> withThreadPoolContext(crossinline block: () -> T): T {
    return suspendCancellableCoroutine {
        debugLog(COROUTINE_HELPER_TAG, "suspendCancellableCoroutine")

        thread {
            try {
                debugLog(COROUTINE_HELPER_TAG, "suspendCancellableCoroutine start")
                it.resume(block())

            } catch (e: Exception) {
                it.resumeWithException(e)

            }
            debugLog(COROUTINE_HELPER_TAG, "suspendCancellableCoroutine resume")
        }
    }
}