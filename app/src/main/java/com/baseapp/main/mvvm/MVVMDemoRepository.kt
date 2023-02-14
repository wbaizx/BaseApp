package com.baseapp.main.mvvm

import com.base.common.base.BaseRepository
import com.baseapp.util.room.User
import com.baseapp.util.room.UserDatabase

class MVVMDemoRepository : BaseRepository() {
    suspend fun insertUsers(user: User): Long = UserDatabase.getUserDao().insertUsers(user)

    suspend fun getAllUsers(): List<User> = UserDatabase.getUserDao().getAllUsers()
}