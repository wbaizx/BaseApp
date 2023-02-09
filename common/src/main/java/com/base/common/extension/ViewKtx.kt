package com.base.common.extension

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.base.common.util.log
import com.chad.library.adapter.base.BaseQuickAdapter

private var lastTime = 0L
private const val lock = "lock"

fun View.setOnSingleGlobalClickListener(event: (View) -> Unit) {
    setOnClickListener {
        synchronized(lock) {
            if (System.currentTimeMillis() - lastTime > 300L) {
                lastTime = System.currentTimeMillis()
                event(it)
            }
        }
    }
}

/**
 * view防止重复点击的扩展方法
 */
inline fun View.setOnSingleClickListener(crossinline event: (View) -> Unit) {
    var lastTime = 0L
    setOnClickListener {
        if (System.currentTimeMillis() - lastTime > 800L) {
            lastTime = System.currentTimeMillis()
            event(it)
        } else {
            log("avoidRepeated", "performClick false")
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