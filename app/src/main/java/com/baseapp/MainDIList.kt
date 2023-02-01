package com.baseapp

import com.baseapp.main.mvvm.mvvmDemoDi
import com.baseapp.main.paging.pagingDemoDi

/**
 * 对应模块的 koin module 添加到这里
 */
val mainDiList = arrayListOf(
    mvvmDemoDi,
    pagingDemoDi
)