package com.base.common.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavCallback
import com.alibaba.android.arouter.launcher.ARouter

/**
 * 创建路由
 */
fun launchARouter(path: String): Postcard = ARouter.getInstance().build(path)

/**
 * 使用路由模式判断登录跳转
 */
fun Postcard.loginNavigation(context: Context, navCallback: NavCallback? = null) {
    val isLogin = getSp().getBoolean(SharedPreferencesUtil.LOGIN, false)

    if (isLogin) {
        normalNavigation(context, navCallback)
    } else {
        showToast("未登录", context)
    }
}

/**
 * 使用路由模式直接跳转，跳过所有拦截器
 */
fun Postcard.normalNavigation(context: Context, navCallback: NavCallback? = null) {
    greenChannel().navigation(context, navCallback)
}

/**
 * 使用普通模式判断登录跳转
 */
fun launchActivityForLogin(context: Context, javaClass: Class<out Activity>) {
    val isLogin = getSp().getBoolean(SharedPreferencesUtil.LOGIN, false)
    if (isLogin) {
        launchActivity(context, javaClass)
    } else {
        showToast("未登录", context)
    }
}

/**
 * 使用普通模式判断登录跳转
 */
fun launchActivityForLogin(context: Context, intent: Intent) {
    val isLogin = getSp().getBoolean(SharedPreferencesUtil.LOGIN, false)
    if (isLogin) {
        launchActivity(context, intent)
    } else {
        showToast("未登录", context)
    }
}

/**
 * 使用普通模式直接跳转
 */
fun launchActivity(context: Context, javaClass: Class<out Activity>) {
    launchActivity(context, Intent(context, javaClass))
}

/**
 * 使用普通模式直接跳转
 */
fun launchActivity(context: Context, intent: Intent) {
    context.startActivity(intent)
}