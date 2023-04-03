package com.baseapp.main.mvvm

import com.base.common.base.BaseRepository
import com.baseapp.util.room.User
import com.baseapp.util.room.UserDatabase

class MVVMDemoRepository : BaseRepository() {
    suspend fun insertUser(user: User): Long = UserDatabase.getUserDao().insertUser(user)

    suspend fun insertUsers(users: List<User>) = UserDatabase.getUserDao().insertUsers(users)

    suspend fun getAllUsers() = UserDatabase.getUserDao().getAllUsers()

    suspend fun getParentTreeUsers(parentIds: List<String>) = UserDatabase.getUserDao().getParentTreeUsers(parentIds)

    suspend fun getChildTreeUsers(ids: List<String>) = UserDatabase.getUserDao().getChildTreeUsers(ids)

    suspend fun updateChildTreeUsers(ids: List<String>, newName: String) = UserDatabase.getUserDao().updateChildTreeUsers(ids, newName)
}