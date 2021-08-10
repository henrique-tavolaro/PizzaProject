package com.example.pizzaproject.domain.interactors

import com.example.pizzaproject.datasource.room.OrderDao
import com.example.pizzaproject.domain.models.OrderInProgress

class DeleteProductFromOrder(
    private val dao: OrderDao
) {

    suspend fun execute(
        product: String
    ){
        dao.deleteProductFromOrder(product)
    }

}