package com.example.ecommerceapplication

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapplication.Model.CategoryModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class CategoryViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val liveData = MutableLiveData<MutableList<CategoryModel>>()
    private val TAG = "Category"

    fun fetch(){
        viewModelScope.launch(Dispatchers.IO) {
            val myService = Service()
            val result = myService.getCategoriesList()
            if (result != null) {
                try {
                    // Parse result string JSON to data class
                    //categories = Klaxon().parse(result)
                    val json = JSONObject(result)
                    val HommeCatList = json.getJSONArray("navigation").getJSONObject(0)
                    val childrenList = HommeCatList.getJSONArray("children").getJSONObject(4).getJSONArray("children")
                    val categories = mutableListOf<CategoryModel>()
                    for (i in 0 until 10) {
                        val categoryObject: JSONObject = childrenList.getJSONObject(i)
                        val content = categoryObject.getJSONObject("content")
                        val title = content.getString("title")
                        val id = categoryObject.getString("id")
                        val imageLink = content.getString("webLargeImageUrl")
                        val category = CategoryModel(id, title, "", imageLink)
                        categories.add(category)
                    }
                    //childrenList.get (0..10)
                    Log.v(TAG, categories.toString())
                    liveData.postValue(categories)
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