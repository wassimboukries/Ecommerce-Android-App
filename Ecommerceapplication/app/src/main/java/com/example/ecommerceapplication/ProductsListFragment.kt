package com.example.ecommerceapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProductsListFragment : Fragment() {

    companion object {
        fun newInstance() = ProductsListFragment()
    }

    private val viewModel: ProductsListViewModel by viewModels()
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapterProductsList: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.products_list_fragment, container, false)
        //val name = ProductFragmentArgs.fromBundle(arguments).productName
        val args: ProductsListFragmentArgs by navArgs()

        val name = args.categoryName
        activity?.title = name;
        (activity as MainActivity?)!!.showUpButton()
        val id = args.categoryId
        //val productName = view.findViewById<TextView>(R.id.productName)
        //productName.text = name

        val layoutManager = GridLayoutManager(context, 2)
        val recyclerView : RecyclerView = view.findViewById(R.id.ProductsRecyclerView)
        recyclerView.layoutManager = layoutManager

        viewModel.fetch(id, true)

        var currentPage : Int = 1;
        viewModel.liveCurrentPage.observe(viewLifecycleOwner) { page ->
            currentPage = page
        }

        viewModel.liveData.observe(viewLifecycleOwner) { products ->
            adapterProductsList = ProductRecyclerViewAdapter(products, viewModel, id, currentPage)
            recyclerView.adapter = adapterProductsList
        }


        return view
    }
}