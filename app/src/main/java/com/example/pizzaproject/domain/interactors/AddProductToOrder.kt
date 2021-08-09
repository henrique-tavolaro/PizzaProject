package com.example.pizzaproject.domain.interactors

import com.example.pizzaproject.datasource.room.OrderDao
import com.example.pizzaproject.domain.data.DataState
import com.example.pizzaproject.domain.models.OrderInProgress
import com.example.pizzaproject.domain.models.Product
import kotlinx.coroutines.flow.Flow

class AddProductToOrder(
    private val dao: OrderDao
) {

    suspend fun execute(
        orderInProgress: OrderInProgress
    ) {
        dao.addProductToOrder(orderInProgress)
    }
}