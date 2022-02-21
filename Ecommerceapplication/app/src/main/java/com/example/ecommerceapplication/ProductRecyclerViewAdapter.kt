package com.example.ecommerceapplication

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerceapplication.Model.ProductModel


class ProductRecyclerViewAdapter(
    private val products: MutableList<ProductModel>,
    private val viewModel: ProductsListViewModel,
    private val categoryId : String,
    private val currentPage : Int
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class ProductsViewHolder (itemView:View): RecyclerView.ViewHolder(itemView) {
        var itemTitle : TextView = itemView.findViewById(R.id.ProductTitle1)
        var itemPrice : TextView = itemView.findViewById(R.id.ProductPrice1)
        var itemRating : RatingBar = itemView.findViewById(R.id.ProductRating1)
        var itemImage : ImageView = itemView.findViewById(R.id.imageProduct1)
        lateinit var itemUrl : String

        init {

            itemView.setOnClickListener {
                val toast = Toast.makeText(itemView.context, itemTitle.text, Toast.LENGTH_LONG)
                toast.show()

                val action = ProductsListFragmentDirections.actionProductFragmentToProductDetailsFragment(itemUrl)
                itemView?.findNavController()?.navigate(action)
            }
        }
    }
    inner class PaginationButtonsViewHolder (itemView:View): RecyclerView.ViewHolder(itemView) {
        var nextButton : Button
        var previousButton : Button
        init {
            nextButton = itemView.findViewById(R.id.NextButton)
            previousButton = itemView.findViewById(R.id.PreviousButton)
        }

    }

    private val TAG = "Products"
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if(viewType == R.layout.button_next){
            var itemView = LayoutInflater.from(parent.context).inflate(R.layout.button_next, parent, false);
            PaginationButtonsViewHolder(itemView)
        } else {
            var itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item_3, parent, false);
            ProductsViewHolder(itemView)
        }

        //val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item_3, parent, false)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == products.size) {
            val paginationButtonsViewHolder = holder as PaginationButtonsViewHolder
            paginationButtonsViewHolder.nextButton!!.setOnClickListener {
                viewModel.fetch(categoryId, true)
            }
            if (currentPage == 1) {
                paginationButtonsViewHolder.previousButton.visibility = INVISIBLE
            } else {
                paginationButtonsViewHolder.previousButton.visibility = VISIBLE
                paginationButtonsViewHolder.previousButton!!.setOnClickListener {
                    viewModel.fetch(categoryId, false)
                }
            }


        } else {
            val productsViewHolder = holder as ProductsViewHolder
            productsViewHolder.itemTitle.text = products[position].name
            productsViewHolder.itemPrice.text = products[position].price + "€"
            productsViewHolder.itemRating.rating = products[position].rating.toFloat()
            productsViewHolder.itemUrl = products[position].url
            val imageLink = products[position].imageLink
            //holder.itemImage.load(imageLink)
            Glide.with(productsViewHolder.itemView.context).load(imageLink).into(productsViewHolder.itemImage);
        }


    }

    override fun getItemCount(): Int {
        return products.size + 1
    }

   override fun getItemViewType(position: Int): Int {
        return if(position === products.size)
            R.layout.button_next
        else
            R.layout.recycler_view_item_3
    }
}
