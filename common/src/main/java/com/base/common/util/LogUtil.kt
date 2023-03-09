package com.base.common.util

import android.util.Log
import com.base.common.isDebug

fun log(tag: String, any: Any?) {
    Log.e(tag, "thread name=${Thread.currentThread().name} -> ${any.toString()}")
}

fun debugLog(tag: String, any: Any?) {
    if (isDebug()) {
        log(tag, "thread name=${Thread.currentThread().name} -> ${any.toString()}")
    }
}