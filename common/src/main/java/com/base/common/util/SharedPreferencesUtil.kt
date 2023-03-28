package com.base.common.util

import android.content.Context
import android.content.SharedPreferences
import com.base.common.getBaseApplication

object SharedPreferencesUtil {
    const val LOGIN = "LOGIN"
}

private val spMap by lazy { HashMap<String, SharedPreferences>() }

fun getSp(key: String = "BASE"): SharedPreferences {
    var sp = spMap[key]
    if (sp == null) {
        sp = getBaseApplication().getSharedPreferences(key, Context.MODE_PRIVATE)
        spMap[key] = sp
    }
    return sp!!
}

inline fun SharedPreferences.applyEdit(action: SharedPreferences.Editor.() -> Unit) {
    edit().apply { action() }.apply()
}