package com.example.ecommerceapplication

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.ecommerceapplication.database.AppDatabase
import com.example.ecommerceapplication.entity.Products
import com.example.ecommerceapplication.entity.UserWithProducts
import com.example.ecommerceapplication.model.ProductModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class ProductsListViewModel(application: Application) : AndroidViewModel(application) {
    // TODO: Implement the ViewModel
    val liveData = MutableLiveData<MutableList<ProductModel>>()
    val liveCurrentPage = MutableLiveData<Int>()
    private var pageNumber : Int = 1
    private val TAG = "Category"
    val liveDataUsers = MutableLiveData<List<UserWithProducts>>()
    val liveIsFavorite = MutableLiveData<Boolean>()
    private val context = getApplication<Application>().applicationContext

    fun fetch(categoryId : String, isNextPage : Boolean?, searchString: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isNextPage != null) if (isNextPage) ++pageNumber else --pageNumber

            val myService = Service()
            val result = myService.getProductsList(categoryId, pageNumber, searchString)
            Log.v(TAG, result)
            if (result != null) {
                try {
                    // Parse result string JSON to data class
                    //categories = Klaxon().parse(result)
                    val json = JSONObject(result)
                    val currentPage = json.getInt("currentPage")
                    liveCurrentPage.postValue(currentPage)
                    val results = json.getJSONArray("products")
                    val products = mutableListOf<ProductModel>()
                    for (i in 0 until results.length()) {
                        val productObject: JSONObject = results.getJSONObject(i)
                        val name = productObject.getString("name")
                        val id = productObject.getInt("sku")
                        val url = productObject.getString("url")
                        val imageLink = productObject.getJSONArray("images").getJSONObject(0).getString("href")
                        val price = productObject.getString("salePrice")
                        var rating = productObject.getString("customerReviewAverage")
                        var reviewCount = productObject.getString("customerReviewCount")
                        /*val name = productObject.getJSONObject("names").getString("title")
                        val id = productObject.getString("sku")
                        val imageLink = productObject.getJSONObject("images").getString("standard")
                        val price = productObject.getJSONObject("prices").getString("current")
                        val rating = productObject.getJSONObject("customerReviews").getString("averageScore")*/
                        if (rating == "null") {
                            rating = "5.";
                        }
                        if (reviewCount == "null") {
                            reviewCount = "0";
                        }

                        val product = ProductModel(id.toString(), name, imageLink, price, rating, reviewCount, url, isProductFavorite(id.toString()))
                        products.add(product)
                    }
                    //childrenList.get (0..10)
                    Log.v(TAG, products.toString())
                    liveData.postValue(products)

                }
                catch(err:Error) {
                    print("Error when parsing JSON: "+err.localizedMessage)
                }
            }
            else {
                print("Error: Get request returned no response")
            }
        }
    }

    fun addFavoriteProduct(productId : String) {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "myUsersDataBase"
        ).build()

        val productsDao = db.favoriteProductsDao()
        val userDao = db.userDao()

        viewModelScope.launch(Dispatchers.IO) {
            productsDao.insert(Products(2, productId.toInt(), 121))

            val userFavorites = userDao.getUserProducts(121)
            liveDataUsers.postValue(userFavorites)
        }
    }

    fun removeFavoriteProduct(productId : String) {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "myUsersDataBase"
        ).build()

        val productsDao = db.favoriteProductsDao()
        val userDao = db.userDao()

        viewModelScope.launch(Dispatchers.IO) {
            productsDao.delete(Products(2, productId.toInt(), 121))

            val userFavorites = userDao.getUserProducts(121)
            liveDataUsers.postValue(userFavorites)
        }
    }

    fun isProductFavorite(productId : String) : Boolean {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "myUsersDataBase"
        ).build()

        val userDao = db.userDao()
        var isFavorite = false
        viewModelScope.launch(Dispatchers.IO) {
            val userFavorites = userDao.getUserProducts(121)
            if (userFavorites.isNotEmpty()) {
                val favorites = userFavorites[0].favoritesList
                val product = favorites.find {
                    it.productId == productId.toInt()
                }
                isFavorite = product != null
            }
        }

        return isFavorite;
    }
}