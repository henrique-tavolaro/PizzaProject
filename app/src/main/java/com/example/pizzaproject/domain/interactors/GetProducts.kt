package com.example.pizzaproject.domain.interactors

import com.example.pizzaproject.datasource.firestore.FirestoreDatasource
import com.example.pizzaproject.domain.data.DataState
import com.example.pizzaproject.domain.models.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class GetProducts(
    private val firestore: FirestoreDatasource
) {

    suspend fun execute(): Flow<DataState<List<Product>>> = flow {
        try {
            emit(DataState.loading())
            val products = firestore.getProducts()

            emit(DataState.success(products))
        } catch (e: Exception) {
            emit(DataState.error<List<Product>>(e.message ?: "Unknown Error"))
        }
    }

}