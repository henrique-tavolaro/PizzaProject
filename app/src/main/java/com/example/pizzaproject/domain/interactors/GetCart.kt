package com.example.pizzaproject.domain.interactors

import com.example.pizzaproject.datasource.room.OrderDao
import com.example.pizzaproject.domain.models.CartDetail
import kotlinx.coroutines.flow.Flow

class GetCart(
    private val dao: OrderDao
) {

    suspend fun execute() : Flow<List<CartDetail>?> {
        return dao.getCart()
    }

}