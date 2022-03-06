package com.example.ecommerceapplication

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.*
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapplication.model.ProductModel

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

        val view = inflater.inflate(R.layout.products_list_fragment, container, false)

        val name = args.categoryName
        activity?.title = name

        if (args.categoryId != null) {
            setHasOptionsMenu(true)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = args.categoryId


        val layoutManager = GridLayoutManager(context, 2)
        val recyclerView : RecyclerView = view.findViewById(R.id.ProductsRecyclerView)
        recyclerView.layoutManager = layoutManager
        val noProductText : TextView = view.findViewById(R.id.noProductsTextView)


        val screenWidth = resources.displayMetrics.run { widthPixels }

        var progressBar : ProgressBar = view.findViewById(R.id.progressBar)

        progressBar.visibility = VISIBLE



        // handling the case where the fragment should display favorite products
        if (id == null) {
            viewModel.getFavoriteProducts()

            viewModel.liveData.observe(viewLifecycleOwner) { userFavorites ->
                progressBar.visibility = GONE

                if (userFavorites.isEmpty()) {
                    recyclerView.visibility = GONE
                    noProductText.visibility = VISIBLE
                } else {
                    adapterProductsList = ProductRecyclerViewAdapter(userFavorites, viewModel, null, null, args.searchString, screenWidth, progressBar)
                    recyclerView.adapter = adapterProductsList

                }
            }
        } else {
            viewModel.fetch(id, null, args.searchString)

            var currentPage : Int = 1;
            viewModel.liveCurrentPage.observe(viewLifecycleOwner) { page ->
                currentPage = page
            }

            viewModel.liveData.observe(viewLifecycleOwner) { products ->
                progressBar.visibility = GONE
                if (products.isEmpty()) {
                    recyclerView.visibility = GONE
                    noProductText.visibility = VISIBLE
                } else {

                    viewModel.isProductFavorite(products)

                    viewModel.liveProductsFavorite.observe(viewLifecycleOwner) { productsWithFav ->
                        adapterProductsList = ProductRecyclerViewAdapter(productsWithFav, viewModel, id, currentPage, args.searchString, screenWidth, progressBar)
                        recyclerView.adapter = adapterProductsList
                    }
                }
            }
        }

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId === R.id.nav_favorite) {
            val action = ProductsListFragmentDirections.actionProductFragmentSelf(null, "Favorite Products", null)
            findNavController().navigate(action)
        }
        return true
    }

}