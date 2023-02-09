package com.login.http

import com.base.common.util.http.BaseHttp

object LoginHttp : BaseHttp() {

    override val baseUrl: String = "https://easy-mock.com/"

    val api by lazy { getApi(LoginAPI::class.java) }
}