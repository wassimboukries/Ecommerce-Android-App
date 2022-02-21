package com.example.ecommerceapplication

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
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
    private val args: ProductsListFragmentArgs by navArgs()
    private lateinit var _inflater : LayoutInflater
    private var _container: ViewGroup? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        _inflater = inflater
        _container = container
        val view = inflater.inflate(R.layout.products_list_fragment, container, false)
        //val name = ProductFragmentArgs.fromBundle(arguments).productName


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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.nav_menu, menu)

        val searchItem = menu.findItem(R.id.nav_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.queryHint = "Search a product"
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                //val viewModel = ProductsListViewModel()
                //viewModel.fetch(args.categoryId, false)
                onCreateView(_inflater, _container, null)
                return true
            }

            override fun onQueryTextChange(p0: String): Boolean {
                //Log.v(TAG, p0)
                return true
            }

        })

    }

}