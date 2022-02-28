package com.example.ecommerceapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ecommerceapplication.dao.UserDao
import com.example.ecommerceapplication.entity.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}