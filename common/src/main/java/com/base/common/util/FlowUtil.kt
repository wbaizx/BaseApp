package com.base.common.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

inline fun <T> Flow<T>.lifecycleCollect(
    owner: LifecycleOwner,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    crossinline call: suspend (T) -> Unit
) {
    owner.lifecycleScope.launch(dispatcher) {
        owner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            distinctUntilChanged().collect { call(it) }
        }
    }
}

fun <T> createMutableStateFlow() = MutableSharedFlow<T>(1, 0, BufferOverflow.DROP_OLDEST)

fun <T> createMutableStateFlow(default: T) = MutableStateFlow(default)
