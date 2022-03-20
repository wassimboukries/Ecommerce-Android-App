package com.example.ecommerceapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.example.ecommerceapplication.ui.products.ProductsListViewModel
import com.example.ecommerceapplication.R
import com.example.ecommerceapplication.model.ProductModel
import com.example.ecommerceapplication.ui.products.ProductsListFragmentDirections
import kotlin.properties.Delegates


class ProductRecyclerViewAdapter(
    private var products: MutableList<ProductModel>,
    private val viewModel: ProductsListViewModel,
    private val categoryId : String?,
    private val currentPage : Int?,
    private val searchRequest : String?,
    private val screenWidth : Int,
    private val progressBar : ProgressBar
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var productsIsFavorite = mutableListOf<Boolean>()

    // we will find in this adapter two different view Holders : the one that holds the products (ProductsViewHolder)
    // and the one that holds the pagination buttons
    // the logic is to display the pagination buttons only if the position in the recycler view is the last one
    // otherwise we display the products


    inner class ProductsViewHolder (itemView:View): RecyclerView.ViewHolder(itemView) {
        var itemTitle : TextView = itemView.findViewById(R.id.ProductTitle1)
        var itemPrice : TextView = itemView.findViewById(R.id.ProductPrice1)
        var itemRating : RatingBar = itemView.findViewById(R.id.ProductRating1)
        var itemImage : ImageView = itemView.findViewById(R.id.imageProduct1)
        var itemReviewCount : TextView = itemView.findViewById(R.id.ProductReviewCount)
        var itemFavorite : ImageView = itemView.findViewById(R.id.Favorite)

        lateinit var itemUrl : String
        lateinit var itemId : String
        var itemIsFavorite by Delegates.notNull<Boolean>()

        init {

            // handle the click on a product ( we navigate into the ProductDetailFragment )
            itemView.setOnClickListener {
                val toast = Toast.makeText(itemView.context, itemTitle.text, Toast.LENGTH_LONG)
                toast.show()
                val action =
                    ProductsListFragmentDirections.actionProductFragmentToProductDetailsFragment(
                        itemUrl,
                        itemTitle.text as String
                    )
                itemView.findNavController().navigate(action)
            }

            // handle the click on the favorite product ( we have two cases, either the product is already a favorite one so on the click
            // it gets removed, either it s not a favorite one so it gets added to the database list
            itemFavorite.setOnClickListener {
                productsIsFavorite[bindingAdapterPosition] = if (productsIsFavorite[bindingAdapterPosition]) {
                    itemFavorite.load(R.drawable.ic_baseline_favorite_border_24)
                    viewModel.removeFavoriteProduct(itemId)
                    false
                } else {
                    itemFavorite.load(R.drawable.ic_outline_favorite_24)
                    viewModel.addFavoriteProduct(products[bindingAdapterPosition])
                    true
                }
            }

        }
    }

    inner class PaginationButtonsViewHolder (itemView:View): RecyclerView.ViewHolder(itemView) {
        var nextButton : Button = itemView.findViewById(R.id.NextButton)
        var previousButton : Button = itemView.findViewById(R.id.PreviousButton)
        var container : ConstraintLayout = itemView.findViewById(R.id.buttonsNextPreviousContainer)

        init {
            val params = container.layoutParams
            params.width = screenWidth
            container.layoutParams = params
            nextButton.setOnClickListener {
                progressBar.visibility = VISIBLE
                viewModel.fetch(categoryId, true, searchRequest)
            }
            if (currentPage == 1) {
                previousButton.visibility = INVISIBLE
            } else {
                previousButton.visibility = VISIBLE
                previousButton.setOnClickListener {
                    progressBar.visibility = VISIBLE
                    viewModel.fetch(categoryId, false, searchRequest)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        // we switch between the two view holders based on the viewType returned by the the function getItemViewType
        return if(viewType == R.layout.button_next && categoryId != null) {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.button_next, parent, false)
            PaginationButtonsViewHolder(itemView)
        } else {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item_3, parent, false)
            // we register if the products are favorites or not based on the database
            for (product in products) {
                productsIsFavorite.add(product.isFavorite)
            }
            ProductsViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads : List<Any>) {
        if(payloads.isNotEmpty()) {
            if (payloads[0] is Boolean) {
                holder as ProductsViewHolder
                productsIsFavorite[position] = payloads[0] as Boolean
                if (!productsIsFavorite[position]) {
                    holder.itemFavorite.load(R.drawable.ic_baseline_favorite_border_24)
                } else {
                    holder.itemFavorite.load(R.drawable.ic_outline_favorite_24)
                }
            }
        } else {
            super.onBindViewHolder(holder,position, payloads);
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position != products.size) {
            val productsViewHolder = holder as ProductsViewHolder
            productsViewHolder.itemTitle.text = products[position].name
            productsViewHolder.itemPrice.text = products[position].price + "€"
            productsViewHolder.itemRating.rating = products[position].rating.toFloat()
            productsViewHolder.itemReviewCount.text = products[position].reviewCount
            productsViewHolder.itemUrl = products[position].url
            productsViewHolder.itemIsFavorite = products[position].isFavorite
            productsViewHolder.itemId = products[position].id

            if (!productsIsFavorite[position]) {
                productsViewHolder.itemFavorite.load(R.drawable.ic_baseline_favorite_border_24)
            } else {
                productsViewHolder.itemFavorite.load(R.drawable.ic_outline_favorite_24)
            }

            val imageLink = products[position].imageLink
            Glide.with(productsViewHolder.itemView.context).load(imageLink).into(productsViewHolder.itemImage);
        } else {
            val buttonsViewHolder = holder as PaginationButtonsViewHolder
            buttonsViewHolder.container.minWidth = screenWidth
        }


    }

    override fun getItemCount(): Int {
        return if (categoryId != null) {
            products.size + 1
        } else {
            products.size
        }
    }

   override fun getItemViewType(position: Int): Int {
        return if(position === products.size)
            R.layout.button_next
        else
            R.layout.recycler_view_item_3
    }
}
