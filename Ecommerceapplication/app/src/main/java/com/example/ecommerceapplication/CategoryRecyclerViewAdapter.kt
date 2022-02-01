package com.example.ecommerceapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.ecommerceapplication.Model.CategoryModel

class CategoryRecyclerViewAdapter(private val categories : MutableList<CategoryModel>) : RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
        var itemText : TextView
        var itemImage : ImageView
        var itemId : String

        init {
            itemText = itemView.findViewById(R.id.textView1)
            itemImage = itemView.findViewById(R.id.imageView1)
            itemId = ""

            itemView.setOnClickListener {
                val toast = Toast.makeText(itemView.context, itemText.text, Toast.LENGTH_LONG)
                toast.show()

                val action = CategoryFragmentDirections.actionCategoryFragmentToProductFragment(itemId, itemText.text.toString())
                itemView?.findNavController()?.navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryRecyclerViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item_2, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CategoryRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.itemText.text = categories[position].title
        var imageInt= categories[position].imageInt
        holder.itemImage.load(imageInt)
        holder.itemId = categories[position].id
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}