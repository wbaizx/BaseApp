package com.base.common.util

import android.content.Context
import android.content.SharedPreferences
import com.base.common.getBaseApplication

object SharedPreferencesUtil {
    const val LOGIN = "LOGIN"
}

private val spMap by lazy { HashMap<String, SharedPreferences>() }

fun getSp(key: String = "BASE"): SharedPreferences {
    return spMap[key] ?: synchronized(spMap) {
        spMap[key] ?: getBaseApplication().getSharedPreferences(key, Context.MODE_PRIVATE).apply {
            spMap[key] = this
        }
    }
}

inline fun SharedPreferences.applyEdit(action: SharedPreferences.Editor.() -> Unit) {
    edit().apply { action() }.apply()
}