package com.example.ecommerceapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapplication.model.CategoryModel
import org.json.JSONArray
import java.io.BufferedReader
import okio.use as use1


class CategoryFragment : Fragment() , FragmentManager.OnBackStackChangedListener{
    private val viewModel: CategoryViewModel by viewModels()
    private val TAG = "Category"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.category_fragment, container, false)
        activity?.title = "Categories";
        activity?.supportFragmentManager?.addOnBackStackChangedListener(this);
        viewModel.addUser()
        viewModel.liveDataUsers.observe(viewLifecycleOwner) { users ->
            Log.v(TAG, users.toString())
        }

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
        val categories = getCategories()

        val adapterCategory = CategoryRecyclerViewAdapter(categories)
        recyclerView.adapter = adapterCategory
    }

    private fun getCategories(): MutableList<CategoryModel> {
        val rawData = JSONArray(context?.assets?.open("categories.json")!!.bufferedReader().use1(BufferedReader::readText))
        val categories : MutableList<CategoryModel> = mutableListOf()
        for (i in 0 until rawData.length()) {
            val jsonCategory = rawData.getJSONObject(i)
            val imageInt = requireContext().resources.getIdentifier(jsonCategory.get("image").toString(), "drawable", requireContext().packageName)
            categories.add(CategoryModel(jsonCategory.get("id").toString(), jsonCategory.get("title").toString(), imageInt))
        }

        return categories
    }

    override fun onBackStackChanged() {
        if (activity != null) {
            // enable Up button only if there are entries on the backstack
            if (requireActivity().supportFragmentManager.backStackEntryCount < 1) {
                (activity as MainActivity?)!!.hideUpButton()
            }
        }
    }

    // to deplace to VieModel class

}