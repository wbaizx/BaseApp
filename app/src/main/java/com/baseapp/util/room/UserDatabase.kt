package com.baseapp.util.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.base.common.getBaseApplication

/**
 * 数据库名
 */
const val DBNAME = "userDatabase.db"

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private val builder by lazy {
            Room.databaseBuilder(getBaseApplication(), UserDatabase::class.java, DBNAME)
                .fallbackToDestructiveMigration()
                .build()
        }

        fun getUserDao() = builder.userDao()
    }
}