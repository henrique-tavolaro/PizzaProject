package com.example.pizzaproject.datasource.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pizzaproject.domain.models.OrderInProgress


@Database(
    entities = [
        OrderInProgress::class
    ], version = 1,
    exportSchema = false
)
abstract class OrderDatabase : RoomDatabase() {
    abstract fun getDao(): OrderDao

    companion object{
        val DATABASE: String = "order_db"
    }
}