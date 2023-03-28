package com.baseapp.main.mvvm

import com.base.common.base.BaseViewModel
import com.base.common.helper.createMutableStateFlow
import com.baseapp.util.room.User

class MVVMDemoViewModel(private val reps: MVVMDemoRepository) : BaseViewModel() {

    val name = createMutableStateFlow("")

    fun saveData() = runTask {
        reps.insertUsers(User(9, "4", 6))
        name.emit("存入成功")
    }

    fun queryData() = runTask(false) {
        name.emit("${reps.getAllUsers().size}")
    }
}