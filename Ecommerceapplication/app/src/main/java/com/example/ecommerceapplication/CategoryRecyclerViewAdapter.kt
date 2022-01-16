package com.example.ecommerceapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

class CategoryRecyclerViewAdapter(private val categories : kotlin.Array<String>, private val images : kotlin.Array<Int>) : RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
        var itemText : TextView
        var itemImage : ImageView

        init {
            itemText = itemView.findViewById(R.id.textView1)
            itemImage = itemView.findViewById(R.id.imageView1)

            itemView.setOnClickListener {
                val toast = Toast.makeText(itemView.context, itemText.text, Toast.LENGTH_LONG)
                toast.show()

                val action = CategoryFragmentDirections.actionCategoryFragmentToProductFragment(itemText.text.toString())
                itemView?.findNavController()?.navigate(action)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryRecyclerViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CategoryRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.itemText.text = categories[position]
        holder.itemImage.setImageResource(images[position])
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}