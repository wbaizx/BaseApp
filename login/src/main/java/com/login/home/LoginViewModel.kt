package com.login.home

import com.base.common.base.BaseViewModel
import com.base.common.helper.createMutableStateFlow
import com.base.common.util.showToast
import com.login.home.bean.LoginBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.ResponseBody

class LoginViewModel(private val reps: LoginRepository) : BaseViewModel() {
    val successBean by lazy { createMutableStateFlow<LoginBean>() }
    val successResponse by lazy { createMutableStateFlow<Pair<ResponseBody, ResponseBody>>() }

    fun loginBean() = runTask(false, {
        showToast("error 拦截")
    }) {
        successBean.emit(reps.loginBean())
    }

    fun loginResponseBody() = runTask {
        val async1 = async(Dispatchers.IO) {
            reps.loginResponseBody()
        }
        val async2 = async(Dispatchers.IO) {
            reps.loginResponseBody()
        }

        //2元组 Pair, 3元组 Triple
        successResponse.emit(Pair(async1.await(), async2.await()))
    }
}