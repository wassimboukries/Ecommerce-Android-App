package com.example.ecommerceapplication

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.*
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
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
        val noProductText : TextView = view.findViewById(R.id.noProductsTextView)

        viewModel.fetch(id, true, args.searchString)

        var currentPage : Int = 1;
        viewModel.liveCurrentPage.observe(viewLifecycleOwner) { page ->
            currentPage = page
        }

        viewModel.liveData.observe(viewLifecycleOwner) { products ->

            if (products.isEmpty()) {
                recyclerView.visibility = GONE
                noProductText.visibility = VISIBLE
            } else {
                adapterProductsList = ProductRecyclerViewAdapter(products, viewModel, id, currentPage, args.searchString)
                recyclerView.adapter = adapterProductsList
            }
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
               /* val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.fragmentContainerView, newInstance())
                transaction?.disallowAddToBackStack()
                transaction?.commit()
                *///onCreateView(_inflater, _container, null)
                val action = ProductsListFragmentDirections.actionProductFragmentSelf(args.categoryId, args.categoryName, p0)
                findNavController().navigate(action)
                return true
            }

            override fun onQueryTextChange(p0: String): Boolean {
                //Log.v(TAG, p0)
                return true
            }

        })

    }

}