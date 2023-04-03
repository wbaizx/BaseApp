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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.coroutines.CoroutineContext

inline fun <T> Flow<T>.stateFlowLifecycleCollect(
    owner: LifecycleOwner,
    dispatcher: CoroutineContext = Dispatchers.Main,
    distinctUntilChanged: Boolean = true,
    crossinline block: suspend (T) -> Unit
) {
    owner.lifecycleScope.safeLaunch(dispatcher) {
        owner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            if (distinctUntilChanged) {
                //Invalid for MutableStateFlow
                distinctUntilChanged().collect { block(it) }
            } else {
                collect { block(it) }
            }
        }
    }
}

fun <T> createMutableStateFlow() = MutableSharedFlow<T>(1, 0, BufferOverflow.DROP_OLDEST)

fun <T> createMutableStateFlow(default: T) = MutableStateFlow(default)
