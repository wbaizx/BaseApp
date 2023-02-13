package com.login.home

import androidx.lifecycle.MutableLiveData
import com.base.common.base.mvvm.BaseMVVMViewModel
import com.login.home.bean.LoginBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.ResponseBody

class LoginViewModel(private val reps: LoginRepository) : BaseMVVMViewModel() {
    val successBean by lazy { MutableLiveData<LoginBean>() }
    val successResponse by lazy { MutableLiveData<Pair<ResponseBody, ResponseBody>>() }

    fun loginBean() = runTask(action = {
        successBean.postValue(reps.loginBean())
    })

    fun loginResponseBody() = runTask(action = {
        val async1 = async(Dispatchers.IO) {
            reps.loginResponseBody()
        }
        val async2 = async(Dispatchers.IO) {
            reps.loginResponseBody()
        }

        //2元组 Pair, 3元组 Triple
        successResponse.postValue(Pair(async1.await(), async2.await()))
    })
}