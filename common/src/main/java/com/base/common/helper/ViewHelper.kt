package com.base.common.helper

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.base.common.util.debugLog

@Volatile
var GLOBAL_CLICK_LAST_TIME = 0L
const val GLOBAL_CLICK_LOOK = "GLOBAL_CLICK_LOOK"
const val SING_TIME_LIMIT = 500L

inline fun checkGlobalClickTime(event: () -> Unit) {
    synchronized(GLOBAL_CLICK_LOOK) {
        if (System.currentTimeMillis() - GLOBAL_CLICK_LAST_TIME > SING_TIME_LIMIT) {
            GLOBAL_CLICK_LAST_TIME = System.currentTimeMillis()
            event()
        }
    }
}

inline fun View.setOnSingleGlobalClickListener(crossinline event: (View) -> Unit) {
    setOnClickListener {
        checkGlobalClickTime {
            event(it)
        }
    }
}


inline fun View.setOnSingleClickListener(crossinline event: (View) -> Unit) {
    var lastTime = 0L
    setOnClickListener {
        if (System.currentTimeMillis() - lastTime > SING_TIME_LIMIT) {
            lastTime = System.currentTimeMillis()
            event(it)
        } else {
            debugLog("avoidRepeated", "performClick false")
        }
    }
}

fun EditText.getFocusAndSoftInput() {
    isFocusable = true
    isFocusableInTouchMode = true
    requestFocus()

    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputManager?.showSoftInput(this, 0)
}

fun EditText.getFocusAndSoftInputFromDialog(window: Window?) {
    isFocusable = true
    isFocusableInTouchMode = true
    requestFocus()

    window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
}

fun Activity.closeSoftInput() {
    val aa = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    aa.hideSoftInputFromWindow(findViewById<View>(android.R.id.content).windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}