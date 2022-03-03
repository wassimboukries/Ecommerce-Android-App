package com.example.ecommerceapplication.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@Entity
data class UserWithProducts (
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "user_creator_id"
    )
    val favoritesList: List<Products>
)