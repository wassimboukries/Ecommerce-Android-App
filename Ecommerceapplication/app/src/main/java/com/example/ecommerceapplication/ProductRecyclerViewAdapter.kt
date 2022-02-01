package com.example.ecommerceapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.recyclerview.widget.RecyclerView;
import coil.load
import com.bumptech.glide.Glide
import com.example.ecommerceapplication.Model.ProductModel

class ProductRecyclerViewAdapter(private val products : MutableList<ProductModel>): RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder (itemView:View): RecyclerView.ViewHolder(itemView) {
        var itemTitle : TextView
        var itemPrice : TextView
        var itemRating : TextView
        var itemImage :ImageView

        init {
            itemTitle = itemView.findViewById(R.id.ProductTitle)
            itemPrice = itemView.findViewById(R.id.ProductPrice)
            itemRating = itemView.findViewById(R.id.ProductRating)
            itemImage = itemView.findViewById(R.id.ProductImage)

            itemView.setOnClickListener {
                val toast = Toast.makeText(itemView.context, itemTitle.text, Toast.LENGTH_LONG)
                toast.show()

                /*val action = CategoryFragmentDirections.actionCategoryFragmentToProductFragment(itemText.text.toString())
                itemView?.findNavController()?.navigate(action)*/
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductRecyclerViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item_1, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ProductRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.itemTitle.text = products[position].name
        holder.itemPrice.text = products[position].price + "€"
        holder.itemRating.text = products[position].rating
        val imageLink = products[position].imageLink
        //holder.itemImage.load(imageLink)
        Glide.with(holder.itemView.context).load(imageLink).into(holder.itemImage);
    }

    override fun getItemCount(): Int {
        return products.size
    }
}
