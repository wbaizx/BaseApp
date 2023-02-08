package com.base.common.util

import android.util.Log
import com.base.common.isDebug

fun log(tag: String, any: Any?) {
    if (isDebug()) {
        Log.e(tag, "thread name=${Thread.currentThread().name} -> ${any.toString()}")
    }
}