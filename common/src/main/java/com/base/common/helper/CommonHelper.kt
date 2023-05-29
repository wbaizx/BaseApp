package com.base.common.helper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.concurrent.locks.ReentrantLock

inline fun <reified T> List<T>.copyList(): List<T> {
    val gson = Gson()
    val json = gson.toJson(this)
    val type = object : TypeToken<List<T>>() {}.type
    return gson.fromJson(json, type)
}

inline fun <T> MutableList<T>.iteratorForEach(action: MutableIterator<T>.(T) -> Unit) {
    iterator().run {
        while (hasNext()) {
            action(next())
        }
    }
}

inline fun <K, T> MutableMap<K, T>.iteratorForEach(action: MutableIterator<MutableMap.MutableEntry<K, T>>.(K, T) -> Unit) {
    iterator().run {
        while (hasNext()) {
            next().let {
                action(it.key, it.value)
            }
        }
    }
}

inline fun <T> ReentrantLock.safeLock(action: () -> T): T {
    try {
        lock()
        return action()
    } finally {
        unlock()
    }
}

fun subCodeContent(content: String, max: Int, suffix: String? = "..."): String {
    val codeLength = content.codePointCount(0, content.length)
    if (codeLength > max) {
        val offsetCode = content.offsetByCodePoints(0, max - 1)
        return "${content.subSequence(0, offsetCode)}$suffix"
    }
    return content
}