package com.login.home

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val LoginHomeDi = module {
    //默认整个app全局单例
    single { LoginRepository() }
    //默认单个activity生命周期内单例
    viewModel { LoginViewModel(get()) }
}