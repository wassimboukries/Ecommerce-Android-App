package com.example.ecommerceapplication.dao

import androidx.room.*
import com.example.ecommerceapplication.entity.Products
import com.example.ecommerceapplication.entity.User
import com.example.ecommerceapplication.entity.UserWithProducts

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Transaction
    @Query("SELECT * FROM user")
    fun getUsersWithProducts(): List<UserWithProducts>

    @Transaction
    @Query("SELECT * FROM user WHERE userId = (:id)")
    fun getUserProducts(id : Int): List<UserWithProducts>

    @Query("SELECT * FROM user WHERE userId IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User

    @Insert
    fun insertAll(vararg users: User)

    @Insert
    fun insert(user: User)

    /*@Query("INSERT INTO user()")
    fun addFavoriteProductToUser(user : User, productId : String)
*/

    @Delete
    fun delete(user: User)
}