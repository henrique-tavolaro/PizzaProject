package com.example.pizzaproject.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_in_progress")
data class OrderInProgress(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "product")
    var product: String,
    @ColumnInfo(name = "price")
    var price: Double

)
