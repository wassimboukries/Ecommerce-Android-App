package com.example.ecommerceapplication

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapplication.Model.CategoryModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.Array
import org.json.JSONObject




class CategoryFragment : Fragment() {
    private val viewModel: CategoryViewModel by viewModels()

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
        val layoutManager = LinearLayoutManager(context)

        val recyclerView : RecyclerView = view.findViewById(R.id.CategoryRecyclerView)
        recyclerView.layoutManager = layoutManager

        viewModel.liveData.observe(viewLifecycleOwner) { categories ->
            val adapterCategory = CategoryRecyclerViewAdapter(categories)
            recyclerView.adapter = adapterCategory
        }
        viewModel.fetch()
    }

    // to deplace to VieModel class

}