package com.login.home

import com.base.common.base.mvvm.BaseMVVMRepository
import com.login.http.LoginHttp

class LoginRepository : BaseMVVMRepository() {
    suspend fun loginBean() = requestNetwork { LoginHttp.api.loginBean() }

    suspend fun loginResponseBody() = requestNetworkBase { LoginHttp.api.loginResponseBody() }
}