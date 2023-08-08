package com.base.common.helper

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 *  think Channel
 */


fun <T> createStateFlow() = MutableSharedFlow<T>(0, 1, BufferOverflow.DROP_OLDEST)

fun <T> createStickStateFlow() = MutableSharedFlow<T>(1, 0, BufferOverflow.DROP_OLDEST)

fun <T> createStickStateFlow(default: T) = MutableStateFlow(default)

/**
 * don't use distinctUntilChanged()
 */
inline fun <T> Flow<T>.distinctLifecycleCollect(owner: LifecycleOwner, crossinline block: suspend (T) -> Unit) =
    owner.lifecycleScope.safeLaunch(Dispatchers.Main) {
        var last: T? = null
        var first = true
        owner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            collect {
                if (first || last != it) {
                    block(it)
                }
                first = false
                last = it
            }
        }
    }

inline fun <T> Flow<T>.lifecycleCollect(owner: LifecycleOwner, crossinline block: suspend (T) -> Unit) =
    owner.lifecycleScope.safeLaunch(Dispatchers.Main) {
        var last: T? = null
        var first = true
        owner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            var resume = true
            collect {
                if (first || !resume || last != it) {
                    block(it)
                }
                resume = false
                first = false
                last = it
            }
        }
    }

//inline fun <T> Flow<T>.distinctCollect(owner: LifecycleOwner, crossinline block: suspend (T) -> Unit) =
//    owner.lifecycleScope.tryLaunch(Dispatchers.Main) {
//        var last: T? = null
//        var first = true
//        collect {
//            if (first || last != it) {
//                block(it)
//            }
//            first = false
//            last = it
//        }
//    }
//
//inline fun <T> Flow<T>.collect(owner: LifecycleOwner, crossinline block: suspend (T) -> Unit) =
//    owner.lifecycleScope.tryLaunch(Dispatchers.Main) {
//        collect {
//            block(it)
//        }
//    }
