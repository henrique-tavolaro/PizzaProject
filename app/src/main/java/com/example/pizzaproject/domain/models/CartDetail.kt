package com.example.pizzaproject.domain.models

import androidx.room.ColumnInfo

data class CartDetail(

    @ColumnInfo(name = "product")
    val product: String,
    @ColumnInfo(name = "sum_price")
    val sumPrice: Double,
    @ColumnInfo(name = "product_count")
    val productCount: Int

)
