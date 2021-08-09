package com.example.pizzaproject.domain.interactors

import com.example.pizzaproject.datasource.room.OrderDao
import kotlinx.coroutines.flow.Flow

class GetOrderTotal(
    private val dao: OrderDao
) {

    fun execute(): Flow<Double?> {
        return dao.getOrderTotal()
    }

}