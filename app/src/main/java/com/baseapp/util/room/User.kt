package com.baseapp.util.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 表名默认为类名
 * 加快查询的索引或索引组，如果添加unique = true，所标记的索引或索引组对应的值则不能重复
 */
@Entity(indices = [Index(value = ["pid"], unique = true)])
data class User(
    //主键是否自动增长，默认为false
    @PrimaryKey(autoGenerate = false) var id: Int,
    val pid: Int,
    val name: String?,
)