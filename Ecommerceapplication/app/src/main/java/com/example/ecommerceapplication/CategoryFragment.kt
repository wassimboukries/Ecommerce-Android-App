package com.example.ecommerceapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class CategoryFragment : Fragment() {
    private val viewModel: CategoryViewModel by viewModels()
    val categoriesNames = arrayOf("TVs & Projectors", "Laptops & Computers", "Apple", "Video Games, Consoles & VR",
        "Cell Phones",
        "Major Appliances" ,
        "Tablets & E-Readers" ,
        "Sound Bars, Speakers & Streaming Devices" ,
        "Headphones" ,
        "PC Gaming" ,
        "Small Appliances, Floor Care & Home Air Quality" ,
        "Wearable Technology" ,
        "Smart Home & Networking" ,
        "Cameras, Camcorders & Drones" ,
        "Toys, Games & Collectibles" ,
        "Printers, Ink & Home Office" ,
        "Health, Fitness & Personal Care" ,
        "Electric Transportation")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.category_fragment, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = GridLayoutManager(context, 2)

        val recyclerView : RecyclerView = view.findViewById(R.id.CategoryRecyclerView)
        recyclerView.layoutManager = layoutManager

        /*viewModel.liveData.observe(viewLifecycleOwner) { categories ->
            val adapterCategory = CategoryRecyclerViewAdapter(categories)
            recyclerView.adapter = adapterCategory
        }*/

        val adapterCategory = CategoryRecyclerViewAdapter(categoriesNames, viewModel.categoriesImagesLinks)
        recyclerView.adapter = adapterCategory
        viewModel.fetch()
    }

    // to deplace to VieModel class

}