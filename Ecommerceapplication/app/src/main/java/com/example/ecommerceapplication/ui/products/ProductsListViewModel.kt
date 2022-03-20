package com.example.ecommerceapplication.ui.products

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.room.Room
import com.example.ecommerceapplication.rest.ApiHelper
import com.example.ecommerceapplication.database.AppDatabase
import com.example.ecommerceapplication.entity.Products
import com.example.ecommerceapplication.model.ProductModel
import kotlinx.coroutines.*
import org.json.JSONObject


class ProductsListViewModel(application: Application) : AndroidViewModel(application) {
    // the products list
    val liveProductsList = MutableLiveData<MutableList<ProductModel>>()
    // which page are we currently displaying
    val liveCurrentPage = MutableLiveData<Int>()
    private var pageNumber : Int = 1
    private val TAG = "Category"
    val liveProductsFavorite = MutableLiveData<MutableList<ProductModel>>()
    private val context = getApplication<Application>().applicationContext

    /**
     * fetches the products that correspond the category Id passed in arg
     * @param categoryId id the category which we seek its products
     * @param isNextPage if we want to fetches the next page of the products of a certain category
     * @param searchString if not null then the user is asking for the products which the names are close to the searchString
     */
    fun fetch(categoryId : String?, isNextPage : Boolean?, searchString: String?) {
        viewModelScope.launch(Dispatchers.IO) {

            if (isNextPage != null) if (isNextPage) ++pageNumber else --pageNumber

            val myService = ApiHelper()
            val result = myService.getProductsList(categoryId, pageNumber, searchString)
            Log.v(TAG, result)
            if (result != null) {
                try {
                    // Parse result string JSON to data class
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

                        // the Api could send null value so we set a value by default to the rating and reviewCount
                        if (rating == "null") {
                            rating = "5.";
                        }
                        if (reviewCount == "null") {
                            reviewCount = "0";
                        }


                        val product = ProductModel(id.toString(), name, imageLink, price, rating, reviewCount, url, false)
                        products.add(product)
                    }
                    liveProductsList.postValue(products)

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

    /**
     * adds the product which id is passed in param to the list of the favorites of the current user
     */
    fun addFavoriteProduct(product : ProductModel) {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "myUsersDataBase"
        ).build()

        val productsDao = db.favoriteProductsDao()

        viewModelScope.launch(Dispatchers.IO) {
            productsDao.insert(Products(0, product.id.toInt(), 121, product.name, product.imageLink, product.price, product.rating, product.reviewCount, product.url))
            db.close()
        }
    }

    /**
     * removes the product which id is passed in param from the list of the favorites of the current user
     */
    fun removeFavoriteProduct(productId : String) {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "myUsersDataBase"
        ).build()

        val productsDao = db.favoriteProductsDao()

        viewModelScope.launch(Dispatchers.IO) {
            productsDao.deleteByProductIdAndUserId(productId.toInt(), 121)
            db.close()
        }
    }

    /**
     * verifies if each one of the products of the product list is labeled as favorite by the user
     * @param products List of the products
     */
    fun isProductFavorite(products : MutableList<ProductModel>) {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "myUsersDataBase"
        ).build()

        viewModelScope.launch(Dispatchers.IO) {
            val userDao = db.userDao()
            // we get all the favorite products of the user
            val userFavorites = userDao.getUserProducts(121)

            // we verify if each product is present in the list
            if (userFavorites.isNotEmpty()) {
                val favorites = userFavorites[0].favoritesList
                for (product in products) {
                    val productFavorite = favorites.find {
                        it.productId == product.id.toInt()
                    }
                    // we set the attribute isFavorite of each product to true or false
                    product.isFavorite = productFavorite != null
                }
            }
            liveProductsFavorite.postValue(products)
            db.close()
        }
    }

    /**
     * fetches favorite products list of the current user
     */
    fun getFavoriteProducts() {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "myUsersDataBase"
        ).build()

        viewModelScope.launch(Dispatchers.IO) {
            val userDao = db.userDao()

            val userFavorites = userDao.getUserProducts(121)
            var favorites : List<Products> = listOf()
            val favoriteProducts : MutableList<ProductModel> = mutableListOf()

            if (userFavorites.isNotEmpty()) {
                favorites = userFavorites[0].favoritesList
                for (favorite in favorites) {
                    favoriteProducts.add(ProductModel(favorite.productId.toString(), favorite.name, favorite.imageLink, favorite.price, favorite.rating, favorite.reviewCount, favorite.url, true))
                }
            }

            liveProductsList.postValue(favoriteProducts)
            db.close()
        }
    }
}