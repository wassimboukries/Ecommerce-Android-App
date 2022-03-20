package com.example.ecommerceapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.ecommerceapplication.R
import com.example.ecommerceapplication.model.CategoryModel
import com.example.ecommerceapplication.ui.category.CategoryFragmentDirections

class CategoryRecyclerViewAdapter(private val categories : MutableList<CategoryModel>) : RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
        var itemText : TextView = itemView.findViewById(R.id.textView1)
        var itemImage : ImageView = itemView.findViewById(R.id.imageView1)
        var itemId : String = ""

        init {

            itemView.setOnClickListener {
                val toast = Toast.makeText(itemView.context, itemText.text, Toast.LENGTH_LONG)
                toast.show()

                val action = CategoryFragmentDirections.actionCategoryFragmentToProductFragment(
                    itemId,
                    itemText.text.toString(),
                    null
                )
                itemView.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item_2, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemText.text = categories[position].title
        val imageInt= categories[position].imageInt
        holder.itemImage.load(imageInt)
        holder.itemId = categories[position].id
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}