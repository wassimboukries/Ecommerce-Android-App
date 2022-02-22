package com.example.ecommerceapplication

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapplication.Model.CategoryModel
import com.example.ecommerceapplication.Model.ProductModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class ProductsListViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val liveData = MutableLiveData<MutableList<ProductModel>>()
    val liveCurrentPage = MutableLiveData<Int>()
    private var pageNumber : Int = 0
    private val TAG = "Category"

    fun fetch(categoryId : String, isNextPage : Boolean, searchString: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val myService = Service()
            if (isNextPage) ++pageNumber else --pageNumber
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
                        val product = ProductModel(id.toString(), name, imageLink, price, rating, reviewCount, url)
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
}