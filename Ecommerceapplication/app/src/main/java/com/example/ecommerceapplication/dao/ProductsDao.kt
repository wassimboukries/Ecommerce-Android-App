package com.example.ecommerceapplication.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.ecommerceapplication.entity.Products
import com.example.ecommerceapplication.entity.User

@Dao
interface ProductsDao {
    @Query("SELECT * FROM products")
    fun getAll(): List<Products>

    @Insert
    fun insertAll(vararg products: Products)

    @Insert
    fun insert(product: Products)

    @Delete
    fun delete(product: Products)

    @Query("DELETE FROM products WHERE (:productId) = productId AND (:userId) = user_creator_id")
    fun deleteByProductIdAndUserId(productId : Int, userId : Int)

}