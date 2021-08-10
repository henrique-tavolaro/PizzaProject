package com.example.pizzaproject.domain.interactors

import com.example.pizzaproject.datasource.room.OrderDao

class ClearCart(
    private val dao: OrderDao
) {

    suspend fun execute(){
        dao.clearCart()
    }

}