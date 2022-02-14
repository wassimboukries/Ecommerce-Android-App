package com.example.ecommerceapplication

import okhttp3.*
import java.io.IOException


class Service {

    var myResponse : String = ""

    fun getCategoriesList(): String {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://api.bestbuy.com/v1/categories?format=json&apiKey=VzGy1xqDaYHeOoAnL6NQwf0O")
            .get()
            .build()


        val response = client.newCall(request).execute()
        myResponse = response.body()!!.string().toString()

            /*.enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException) {
                e.printStackTrace()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call?, response: Response) {
                if (response.isSuccessful) {
                    myResponse = response.body()!!.string()
                    print(myResponse)
                }
            }
        })*/

        return myResponse;
    }

    fun getCategory(categoryName: String) : String {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://api.bestbuy.com/v1/categories(name=$categoryName)?format=json&apiKey=VzGy1xqDaYHeOoAnL6NQwf0O")
            .get()
            .build()


        val response = client.newCall(request).execute()
        myResponse = response.body()!!.string().toString()

        return myResponse
    }

    fun getProductsList(categoryId : String, pageNumber : Int): String {
        val client = OkHttpClient()

        var paginationString = ""
        if (pageNumber != 0) {
            paginationString = "&page=$pageNumber"
        }


        val request = Request.Builder()
            .url("https://api.bestbuy.com/v1/products(categoryPath.id=$categoryId)?format=json$paginationString&apiKey=VzGy1xqDaYHeOoAnL6NQwf0O")
            .get()
            .build()


        val response = client.newCall(request).execute()
        myResponse = response.body()!!.string().toString()

        return myResponse
    }
}