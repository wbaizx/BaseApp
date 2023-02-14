package com.baseapp.main.mvvm

import androidx.lifecycle.MutableLiveData
import com.base.common.base.mvvm.BaseMVVMViewModel
import com.baseapp.util.room.User

class MVVMDemoViewModel(private val reps: MVVMDemoRepository) : BaseMVVMViewModel() {

    val name by lazy { MutableLiveData<String>() }

    fun saveData() = runTask {
        reps.insertUsers(User(9, "4", 6))
        name.postValue("存入成功")
    }

    fun queryData() = runTask(false) {
        name.postValue("${reps.getAllUsers().size}")
    }
}