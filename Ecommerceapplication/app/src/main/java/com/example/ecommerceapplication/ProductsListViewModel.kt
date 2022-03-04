package com.example.ecommerceapplication

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.room.Room
import com.example.ecommerceapplication.database.AppDatabase
import com.example.ecommerceapplication.entity.Products
import com.example.ecommerceapplication.entity.UserWithProducts
import com.example.ecommerceapplication.model.ProductModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.json.JSONObject


class ProductsListViewModel(application: Application) : AndroidViewModel(application) {
    // TODO: Implement the ViewModel
    val liveData = MutableLiveData<MutableList<ProductModel>>()
    val liveCurrentPage = MutableLiveData<Int>()
    private var pageNumber : Int = 1
    private val TAG = "Category"
    val liveDataUsers = MutableLiveData<List<UserWithProducts>>()
    val liveProductsFavorite = MutableLiveData<MutableList<ProductModel>>()
    private val context = getApplication<Application>().applicationContext

    val viewModelJob = Job()
    val viewModelCoroutineScope = CoroutineScope(Dispatchers.IO + viewModelJob)

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


                        val product = ProductModel(id.toString(), name, imageLink, price, rating, reviewCount, url, false)
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
            productsDao.insert(Products(0, productId.toInt(), 121))

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

        viewModelCoroutineScope.launch(Dispatchers.IO) {
            productsDao.delete(Products(0, productId.toInt(), 121))

            val userFavorites = userDao.getUserProducts(121)
            liveDataUsers.postValue(userFavorites)
            db.close()
        }


    }

    fun isProductFavorite(products : MutableList<ProductModel>) {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "myUsersDataBase"
        ).build()

        viewModelCoroutineScope.launch(Dispatchers.IO) {
            val userDao = db.userDao()
            val userFavorites = userDao.getUserProducts(121)

            if (userFavorites.isNotEmpty()) {
                for (product in products) {

                        val favorites = userFavorites[0].favoritesList
                        val productFavorite = favorites.find {
                            it.productId == product.id.toInt()
                        }
                        product.isFavorite = productFavorite != null
                }
                liveProductsFavorite.postValue(products)
            }
            db.close()
        }



    }
}