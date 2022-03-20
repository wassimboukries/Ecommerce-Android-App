package com.example.ecommerceapplication.rest

import okhttp3.*


class ApiHelper {

    var myResponse : String = ""

    /**
     * fetches the products of a certain category from the Best BUY API, we can indicate which page number we want to display
     * we can also search for the products which name matches the pattern in the searchString
     */
    fun getProductsList(categoryId : String?, pageNumber : Int, searchString: String?): String {
        val client = OkHttpClient()

        var paginationString = ""
        if (pageNumber != 1) {
            paginationString = "&page=$pageNumber"
        }

        var search = ""
        if (searchString != null) {
            search = "&search=$searchString"
        }

        val request = Request.Builder()
            .url("https://api.bestbuy.com/v1/products(categoryPath.id=$categoryId$search)?format=json$paginationString&apiKey=VzGy1xqDaYHeOoAnL6NQwf0O")
            .get()
            .build()


        val response = client.newCall(request).execute()
        myResponse = response.body()!!.string().toString()

        return myResponse
    }
}