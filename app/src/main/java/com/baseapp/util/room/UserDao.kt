package com.baseapp.util.room

import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<User>): List<Long>


    //---------------------------------------------
    @Delete
    suspend fun deleteUsers(user: User): Int

    @Delete
    suspend fun deleteUsers(users: List<User>): Int

    @Query("DELETE FROM User WHERE id=:id")
    suspend fun deleteUsers(id: Int): Int


    //---------------------------------------------
    @Update
    suspend fun updateUsers(user: User): Int

    @Update
    suspend fun updateUsers(users: List<User>): Int

    @Query("UPDATE User SET name=:name WHERE id=:id")
    suspend fun updateName(name: String, id: Int): Int


    //---------------------------------------------
    @Query("SELECT * FROM User")
    suspend fun getAllUsers(): List<User>

    /**
     * 查询树形结构给定节点的所有父节点
     * 注意，结果中会包含 parentIds 自身，所以应当传的是第一层父级节点列表
     */
    @Query(
        "WITH RECURSIVE parent_find AS " +
                "(" +
                "SELECT User.* FROM User WHERE User.id IN (:parentIds) " +
                "UNION " +
                "SELECT User.* FROM parent_find JOIN User ON User.id = parent_find.pid" +
                ") " +
                "SELECT * FROM parent_find ORDER BY id ASC"
    )
    suspend fun getParentTreeUsers(parentIds: List<String>): List<User>

    /**
     * 查询树形结构给定节点的所有子节点
     * 不包含自身
     */
    @Query(
        "WITH RECURSIVE child_find AS " +
                "(" +
                "SELECT User.* FROM User WHERE User.pid IN (:ids) " +
                "UNION " +
                "SELECT User.* FROM child_find JOIN User ON User.pid = child_find.id" +
                ") " +
                "SELECT * FROM child_find ORDER BY id DESC"
    )
    suspend fun getChildTreeUsers(ids: List<String>): List<User>

    /**
     * 查询树形结构给定节点的所有子节点，可自定义最终 WHERE 条件
     * 不包含自身
     */
    @Query(
        "WITH RECURSIVE child_find AS " +
                "(" +
                "SELECT User.* FROM User WHERE User.pid IN (:ids) AND:where " +
                "UNION " +
                "SELECT User.* FROM child_find JOIN User ON User.pid = child_find.id WHERE:where" +
                ") " +
                "SELECT * FROM child_find WHERE:where"
    )
    suspend fun getChildTreeUsers(ids: List<String>, where: String): List<User>

    /**
     * 更新树形结构给定节点的所有子节点
     * 不包含自身
     */
    //条件更新
//    updateSb.append("update $dbName set $columnIsTrash = 1")
//    updateSb.append(", $columnRecycledTime = case when $columnRecycledTime = 0 then $newRecycledTime else $columnRecycledTime end")
//    updateSb.append(" where $columnNodeId in ($rawSb)")
    @Query(
        "UPDATE User SET name=:newName WHERE id IN (" +
                "WITH RECURSIVE child_find AS " +
                "(" +
                "SELECT User.id FROM User WHERE User.pid IN (:ids) " +
                "UNION ALL " +
                "SELECT User.id FROM child_find JOIN User ON User.pid = child_find.id" +
                ") " +
                "SELECT id FROM child_find)"
    )
    suspend fun updateChildTreeUsers(ids: List<String>, newName: String): Int
}