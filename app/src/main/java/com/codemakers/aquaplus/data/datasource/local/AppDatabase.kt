package com.codemakers.aquaplus.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codemakers.aquaplus.data.datasource.local.dao.UserDao

@Database(
    entities = [],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}