package com.example.ecommerceapplication

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon
import com.example.ecommerceapplication.Model.CategoryModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.Array
import org.json.JSONObject




class CategoryFragment : Fragment() {

    companion object {
        fun newInstance() = CategoryFragment()
    }

    private val TAG = "Category"
    private lateinit var viewModel: CategoryViewModel
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapterCategory: RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.category_fragment, container, false)
        layoutManager = LinearLayoutManager(context)

        val recyclerView : RecyclerView = view.findViewById(R.id.CategoryRecyclerView)
        recyclerView.layoutManager = layoutManager

        fetch("")

        var categories = arrayOf("Informatique", "Beauté", "Hi-tech", "Bullshit", "Clothes", "Informatique", "Beauté", "Hi-tech", "Bullshit", "Clothes", "Beauté", "Hi-tech", "Bullshit", "Clothes" )
        var categoriesImages = arrayOf(R.drawable.ic_baseline_laptop_24, R.drawable.ic_baseline_laptop_24,
            R.drawable.ic_baseline_laptop_24, R.drawable.ic_baseline_laptop_24, R.drawable.ic_baseline_laptop_24, R.drawable.ic_baseline_laptop_24, R.drawable.ic_baseline_laptop_24,
            R.drawable.ic_baseline_laptop_24, R.drawable.ic_baseline_laptop_24, R.drawable.ic_baseline_laptop_24, R.drawable.ic_baseline_laptop_24, R.drawable.ic_baseline_laptop_24, R.drawable.ic_baseline_laptop_24, R.drawable.ic_baseline_laptop_24)

        adapterCategory = CategoryRecyclerViewAdapter(categories, categoriesImages)
        recyclerView.adapter = adapterCategory

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun fetch(sUrl: String): Array? {
        var categories: Array? = null
        lifecycleScope.launch(Dispatchers.IO) {
            val myService = Service()
            val result = myService.getCategoriesList()
            if (result != null) {
                try {
                    // Parse result string JSON to data class
                    //categories = Klaxon().parse(result)
                    val json = JSONObject(result)
                    val brandsList = json.getJSONArray("brands")
                    Log.v(TAG, brandsList.toString())
                }
                catch(err:Error) {
                    print("Error when parsing JSON: "+err.localizedMessage)
                }
            }
            else {
                print("Error: Get request returned no response")
            }
        }
        return categories
    }
}