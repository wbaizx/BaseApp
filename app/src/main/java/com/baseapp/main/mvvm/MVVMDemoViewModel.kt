package com.baseapp.main.mvvm

import com.base.common.base.BaseViewModel
import com.baseapp.util.room.User
import kotlinx.coroutines.flow.MutableStateFlow

class MVVMDemoViewModel(private val reps: MVVMDemoRepository) : BaseViewModel() {

    val name by lazy { MutableStateFlow("") }

    fun saveData() = runTask {
        reps.insertUsers(User(9, "4", 6))
        name.emit("存入成功")
    }

    fun queryData() = runTask(false) {
        name.emit("${reps.getAllUsers().size}")
    }
}