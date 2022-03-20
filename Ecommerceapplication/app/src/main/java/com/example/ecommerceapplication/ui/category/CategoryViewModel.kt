package com.example.ecommerceapplication.ui.category

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.room.Room
import com.example.ecommerceapplication.entity.User
import com.example.ecommerceapplication.model.CategoryModel
import com.example.ecommerceapplication.database.AppDatabase
import com.example.ecommerceapplication.entity.UserWithProducts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel(application: Application) : AndroidViewModel(application){
    /**
     * adds the current user to the database in case he's not there yet
     */
    fun addUser() {
        val db = Room.databaseBuilder(
            getApplication<Application>().applicationContext,
            AppDatabase::class.java, "myUsersDataBase"
        ).build()

        val userDao = db.userDao()
        val myUser = User(121, "Wassim", "Boukries", "wassim-boukries@live.fr")

        viewModelScope.launch(Dispatchers.IO) {

            // we verify if the user is already signed in the device's database, if not we add it
            val user = userDao.getUserProducts(121)
            if (user.isEmpty()) {
                userDao.insert(myUser)
            }
        }

    }
}