package com.example.ecommerceapplication.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Products(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo val productId : Int,
    @ColumnInfo(name = "user_creator_id") val userCreatorId: Int
)
