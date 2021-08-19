package com.example.pizzaproject.domain.interactors

import com.example.pizzaproject.datasource.firestore.FirestoreDatasource
import com.example.pizzaproject.domain.data.DataState
import com.example.pizzaproject.domain.models.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetOrders(
    private val firestore: FirestoreDatasource
) {

    suspend fun execute(
        clientId: String
    ): Flow<DataState<List<Order>>> = flow {
        try {
//            emit(DataState.loading())
            val orders = firestore.getOrders(clientId)

            emit(DataState.success(orders))
        } catch (e: Exception) {
            emit(DataState.error<List<Order>>(e.message ?: "Unknown Error"))
        }


    }

}