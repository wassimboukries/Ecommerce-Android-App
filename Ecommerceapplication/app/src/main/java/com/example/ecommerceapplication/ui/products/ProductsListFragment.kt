package com.example.ecommerceapplication.ui.products

import android.os.Bundle
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
import com.example.ecommerceapplication.R
import com.example.ecommerceapplication.adapter.ProductRecyclerViewAdapter

class ProductsListFragment : Fragment() {

    companion object {
        fun newInstance() = ProductsListFragment()
    }

    private val viewModel: ProductsListViewModel by viewModels()
    private var adapterProductsList: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null
    private val args: ProductsListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.products_list_fragment, container, false)

        val name = args.categoryName
        activity?.title = name

        // we display the search bar and the menu if we are the fragment is displaying products of a specific category,
        // args.categoryId will be null if the fragment displays the favorite products
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

        val progressBar : ProgressBar = view.findViewById(R.id.progressBar)

        progressBar.visibility = VISIBLE



        // handling the case where the fragment should display favorite products
        if (id == null) {
            viewModel.getFavoriteProducts()

            viewModel.liveProductsList.observe(viewLifecycleOwner) { userFavorites ->
                progressBar.visibility = GONE

                // we display the message that there are no favorite products in case there aren't any,
                // otherwise we call the adapter with these favorites
                if (userFavorites.isEmpty()) {
                    recyclerView.visibility = GONE
                    noProductText.visibility = VISIBLE
                } else {
                    adapterProductsList = ProductRecyclerViewAdapter(userFavorites, viewModel, null, null, args.searchString, screenWidth, progressBar)
                    recyclerView.adapter = adapterProductsList

                }
            }
          // if we have to display the products of a category
        } else {
            viewModel.fetch(id, null, args.searchString)

            // we update the current page that we are currently displaying
            var currentPage = 1;
            viewModel.liveCurrentPage.observe(viewLifecycleOwner) { page ->
                currentPage = page
            }

            viewModel.liveProductsList.observe(viewLifecycleOwner) { products ->
                progressBar.visibility = GONE
                if (products.isEmpty()) {
                    recyclerView.visibility = GONE
                    noProductText.visibility = VISIBLE
                } else {
                    // we verify if each product of the list is favorite or not (so we can know which icon of the favorite to display)
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
                // we navigate into the same fragment (ProductsListFragment) with the search string passed in args
                val action =
                    ProductsListFragmentDirections.actionProductFragmentSelf(
                        args.categoryId,
                        args.categoryName,
                        p0
                    )
                findNavController().navigate(action)
                return true
            }

            override fun onQueryTextChange(p0: String): Boolean {
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId === R.id.nav_favorite) {
            // we navigate into the same fragment (ProductsListFragment) with category id to null,
            // because we are displaying favorite products
            val action =
                ProductsListFragmentDirections.actionProductFragmentSelf(
                    null,
                    "Favorite Products",
                    null
                )
            findNavController().navigate(action)
        }
        return true
    }

}