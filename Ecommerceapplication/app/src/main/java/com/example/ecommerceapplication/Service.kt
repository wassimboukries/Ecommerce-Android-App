package com.example.ecommerceapplication

import okhttp3.*
import java.io.IOException


class Service {

    var myResponse : String = ""

    fun getCategoriesList(): String {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://asos2.p.rapidapi.com/categories/list?country=FR&lang=fr-FR")
            .get()
            .addHeader("x-rapidapi-host", "asos2.p.rapidapi.com")
            .addHeader("x-rapidapi-key", "24c331a03bmsh3957030eff8b15ap1d8e18jsnb9f963404616")
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
}