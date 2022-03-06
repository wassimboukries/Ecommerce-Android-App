package com.example.ecommerceapplication

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.room.Room
import com.example.ecommerceapplication.entity.User
import com.example.ecommerceapplication.model.CategoryModel
import com.example.ecommerceapplication.database.AppDatabase
import com.example.ecommerceapplication.entity.Products
import com.example.ecommerceapplication.entity.UserWithProducts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel(application: Application) : AndroidViewModel(application){
    // TODO: Implement the ViewModel
    val liveData = MutableLiveData<MutableList<CategoryModel>>()
    val liveDataUsers = MutableLiveData<List<UserWithProducts>>()
    private val context = getApplication<Application>().applicationContext
    private val TAG = "myDB"

    fun addUser() {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "myUsersDataBase"
        ).build()

        val userDao = db.userDao()
        val productsDao = db.favoriteProductsDao()

        val favoriteProducts : MutableList<String> = mutableListOf("aaa")
        val myUser = User(121, "Wassim", "Boukries", "wassim-boukries@live.fr")

        viewModelScope.launch(Dispatchers.IO) {
            userDao.insert(myUser)

            //productsDao.insert(Products(7878787, 8,  121))
            //productsDao.insert(Products(787, 8,  125))

            val users = userDao.getUsersWithProducts()
            Log.v(TAG, users.toString())
            liveDataUsers.postValue(users)
        }

    }
}