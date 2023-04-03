package com.baseapp.main.proxy

import java.net.Authenticator
import java.net.PasswordAuthentication

/**
 * 使用ThreadLocal将代理账号信息绑定在当前线程
 * 注意在协程中，suspend调度回来后未必与初始调用在同一线程
 */
object ProxyAuthenticator : Authenticator() {
    private val credentials: ThreadLocal<PasswordAuthentication> = ThreadLocal()

    init {
        Authenticator.setDefault(this)
    }

    fun setPasswordAuthentication(userName: String, password: String) {
        credentials.set(PasswordAuthentication(userName, password.toCharArray()))
    }

    override fun getPasswordAuthentication(): PasswordAuthentication {
        return credentials.get() ?: super.getPasswordAuthentication()
    }
}