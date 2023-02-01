package com.baseapp.main.mvvm

import com.base.common.base.mvvm.BaseMVVMRepository
import com.baseapp.util.room.User
import com.baseapp.util.room.UserDatabase

class MVVMDemoRepository : BaseMVVMRepository() {
    suspend fun insertUsers(user: User): Long = UserDatabase.DB_USER.insertUsers(user)

    suspend fun getAllUsers(): List<User> = UserDatabase.DB_USER.getAllUsers()
}