package com.example.ecommerceapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ecommerceapplication.DAO.UserDao
import com.example.ecommerceapplication.Entity.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}