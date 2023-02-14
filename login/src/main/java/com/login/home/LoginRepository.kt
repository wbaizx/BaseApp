package com.login.home

import com.base.common.base.BaseRepository
import com.login.http.LoginHttp

class LoginRepository : BaseRepository() {
    suspend fun loginBean() = requestNetwork { LoginHttp.api.loginBean() }

    suspend fun loginResponseBody() = requestNetworkBase { LoginHttp.api.loginResponseBody() }
}