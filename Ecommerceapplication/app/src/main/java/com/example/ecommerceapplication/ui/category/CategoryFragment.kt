package com.example.ecommerceapplication.ui.category

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapplication.R
import com.example.ecommerceapplication.adapter.CategoryRecyclerViewAdapter
import com.example.ecommerceapplication.model.CategoryModel
import org.json.JSONArray
import java.io.BufferedReader
import okio.use as use1


class CategoryFragment : Fragment(){
    private val viewModel: CategoryViewModel by viewModels()
    private val TAG = "Category"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.category_fragment, container, false)
        activity?.title = "Categories";

        // adds the current user to the database in case he's not there yet
        viewModel.addUser()
        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = GridLayoutManager(context, 2)

        val recyclerView : RecyclerView = view.findViewById(R.id.CategoryRecyclerView)
        recyclerView.layoutManager = layoutManager

        //fetches the categories image, name and id from a local json file
        val categories = getCategories()

        val adapterCategory = CategoryRecyclerViewAdapter(categories)
        recyclerView.adapter = adapterCategory
    }

    /**
     * fetches the categories image, name and id from a local json file
     */
    private fun getCategories(): MutableList<CategoryModel> {
        //we open and read the categories.json file
        val rawData = JSONArray(context?.assets?.open("categories.json")!!.bufferedReader().use1(BufferedReader::readText))
        val categories : MutableList<CategoryModel> = mutableListOf()
        // then we add each category to the list that we will eventually return
        for (i in 0 until rawData.length()) {
            val jsonCategory = rawData.getJSONObject(i)
            val imageInt = requireContext().resources.getIdentifier(jsonCategory.get("image").toString(), "drawable", requireContext().packageName)
            categories.add(CategoryModel(jsonCategory.get("id").toString(), jsonCategory.get("title").toString(), imageInt))
        }

        return categories
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_nav_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId === R.id.nav_favorite) {
            // we navigate into the ProductsListFragment to display the favorite products
            val action =
                CategoryFragmentDirections.actionCategoryFragmentToProductFragment(
                    null,
                    "Favorite Products",
                    null
                )
            findNavController().navigate(action)
        }
        return true
    }

}