package com.baseapp.main.paging

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val pagingDemoDi = module {
    //默认整个app全局单例
    single { PagingRepository() }
    //默认单个activity生命周期内单例
    viewModel { PagingViewModel(get()) }
}