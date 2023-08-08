package com.baseapp.main.mvvm

import com.base.common.base.BaseViewModel
import com.base.common.helper.createStickStateFlow
import com.base.common.util.debugLog
import com.baseapp.util.room.User

private const val ROOM_TAG = "ROOM_TAG"

class MVVMDemoViewModel(private val reps: MVVMDemoRepository) : BaseViewModel() {

    val name = createStickStateFlow("")

    fun saveData() = runTask {
        val ls = arrayListOf<User>()
        repeat(5) {
            val index = it
            ls.add(User(index, index - 1, "name $index"))
        }

        repeat(5) {
            val index = it + 10
            ls.add(User(index, index - 1, "name $index"))
        }

        val insert = reps.insertUsers(ls)

        debugLog(ROOM_TAG, insert)

        name.emit("存入成功")
    }

    fun queryData() = runTask(false) {
        debugLog(ROOM_TAG, "getParentTreeUsers  ${reps.getParentTreeUsers(listOf("1", "13")).map { it.id }}")
        debugLog(ROOM_TAG, "getChildTreeUsers  ${reps.getChildTreeUsers(listOf("1", "11")).map { it.id }}")
        debugLog(ROOM_TAG, "updateChildTreeUsers  ${reps.updateChildTreeUsers(listOf("1", "11"), "new")}")
        debugLog(ROOM_TAG, "updateChildTreeUsers  ${reps.getAllUsers().map { it.name }}")

        name.emit("${reps.getAllUsers().size}")
    }
}