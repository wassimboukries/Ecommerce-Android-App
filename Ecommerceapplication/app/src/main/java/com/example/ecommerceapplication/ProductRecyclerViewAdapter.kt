package com.example.ecommerceapplication

import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerceapplication.Model.ProductModel


class ProductRecyclerViewAdapter(
    private val products: MutableList<ProductModel>,
    private val viewModel: ProductsListViewModel,
    private val categoryId : String,
    private val currentPage : Int,
    private val searchRequest : String?,
    private val screenWidth : Int
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class ProductsViewHolder (itemView:View): RecyclerView.ViewHolder(itemView) {
        var itemTitle : TextView = itemView.findViewById(R.id.ProductTitle1)
        var itemPrice : TextView = itemView.findViewById(R.id.ProductPrice1)
        var itemRating : RatingBar = itemView.findViewById(R.id.ProductRating1)
        var itemImage : ImageView = itemView.findViewById(R.id.imageProduct1)
        var itemReviewCount : TextView = itemView.findViewById(R.id.ProductReviewCount)

        lateinit var itemUrl : String

        init {

            itemView.setOnClickListener {
                val toast = Toast.makeText(itemView.context, itemTitle.text, Toast.LENGTH_LONG)
                toast.show()
                /*val intent = Intent(Intent.ACTION_VIEW, Uri.parse(itemUrl))
                startActivity(itemView.context, intent, null);*/
                val action = ProductsListFragmentDirections.actionProductFragmentToProductDetailsFragment(itemUrl,
                    itemTitle.text as String
                )
                itemView.findNavController().navigate(action)
            }
        }
    }
    inner class PaginationButtonsViewHolder (itemView:View): RecyclerView.ViewHolder(itemView) {
        var nextButton : Button = itemView.findViewById(R.id.NextButton)
        var previousButton : Button = itemView.findViewById(R.id.PreviousButton)
        var container : ConstraintLayout = itemView.findViewById(R.id.buttonsNextPreviousContainer)

        init {
            var params = container.layoutParams
            params.width = screenWidth
            container.layoutParams = params
            nextButton.setOnClickListener {
                viewModel.fetch(categoryId, true, searchRequest)
            }
            if (currentPage == 1) {
                previousButton.visibility = INVISIBLE
            } else {
                previousButton.visibility = VISIBLE
                previousButton.setOnClickListener {
                    viewModel.fetch(categoryId, false, searchRequest)
                }
            }
        }
    }

    private val TAG = "Products"
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if(viewType == R.layout.button_next){
            val layout = R.layout.button_next
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.button_next, parent, false);
            PaginationButtonsViewHolder(itemView)
        } else {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item_3, parent, false);
            ProductsViewHolder(itemView)
        }

        //val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item_3, parent, false)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position != products.size) {
            val productsViewHolder = holder as ProductsViewHolder
            productsViewHolder.itemTitle.text = products[position].name
            productsViewHolder.itemPrice.text = products[position].price + "€"
            productsViewHolder.itemRating.rating = products[position].rating.toFloat()
            productsViewHolder.itemReviewCount.text = products[position].reviewCount
            productsViewHolder.itemUrl = products[position].url
            val imageLink = products[position].imageLink
            //holder.itemImage.load(imageLink)
            Glide.with(productsViewHolder.itemView.context).load(imageLink).into(productsViewHolder.itemImage);
        } else {
            val buttonsViewHolder = holder as PaginationButtonsViewHolder
            buttonsViewHolder.container.minWidth = screenWidth.toInt()
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
