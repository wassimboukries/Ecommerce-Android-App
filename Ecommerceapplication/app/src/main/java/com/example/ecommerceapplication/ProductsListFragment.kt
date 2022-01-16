package com.example.ecommerceapplication

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProductsListFragment : Fragment() {

    companion object {
        fun newInstance() = ProductsListFragment()
    }

    private lateinit var viewModel: ProductsListViewModel
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapterProductsList: RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.products_list_fragment, container, false)
        //val name = ProductFragmentArgs.fromBundle(arguments).productName
        val args: ProductsListFragmentArgs by navArgs()

        val name = args.productName
        val productName = view.findViewById<TextView>(R.id.productName)
        productName.text = name

        layoutManager = LinearLayoutManager(context)

        val recyclerView : RecyclerView = view.findViewById(R.id.ProductsRecyclerView)
        recyclerView.layoutManager = layoutManager

        var products = arrayOf("Pc Portable MSI", "Pc Portable HP", "Pc Portable Lenovo", "Pc Portable Asus", "Pc Portable AlienWare", "Pc Portable GIGABYTE", "Pc Portable Toshiba", "Pc Portable ChromeBook", "Pc Bureau MSI", "Pc Bureau HP", "Pc Bureau LG", "PC bureau Bullshit", "Pc laptop", "Pc Rogue" )
        var categoriesImages = arrayOf(R.drawable.ic_baseline_laptop_24, R.drawable.ic_baseline_laptop_24,
            R.drawable.ic_baseline_laptop_24, R.drawable.ic_baseline_laptop_24, R.drawable.ic_baseline_laptop_24, R.drawable.ic_baseline_laptop_24, R.drawable.ic_baseline_laptop_24,
            R.drawable.ic_baseline_laptop_24, R.drawable.ic_baseline_laptop_24, R.drawable.ic_baseline_laptop_24, R.drawable.ic_baseline_laptop_24, R.drawable.ic_baseline_laptop_24, R.drawable.ic_baseline_laptop_24, R.drawable.ic_baseline_laptop_24)

        adapterProductsList = ProductRecyclerViewAdapter(products, categoriesImages)
        recyclerView.adapter = adapterProductsList

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProductsListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}