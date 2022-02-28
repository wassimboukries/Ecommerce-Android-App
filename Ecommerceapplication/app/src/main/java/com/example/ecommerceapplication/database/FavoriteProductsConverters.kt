package com.example.ecommerceapplication.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class FavoriteProductsConverters {
    @TypeConverter
    fun fromFavoriteProductsList(countryLang: List<String?>?): String? {
        if (countryLang == null) {
            return null
        }
        val gson = Gson()
        val type: Type =
            object : TypeToken<List<String?>?>() {}.type
        return gson.toJson(countryLang, type)
    }

    @TypeConverter
    fun toCountryLangList(countryLangString: String?): List<String>? {
        if (countryLangString == null) {
            return null
        }
        val gson = Gson()
        val type: Type =
            object : TypeToken<List<String?>?>() {}.type
        return gson.fromJson(countryLangString, type)
    }
}