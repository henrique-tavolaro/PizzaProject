package com.example.pizzaproject.datasource.firestore

import com.example.pizzaproject.domain.models.Client
import com.example.pizzaproject.domain.models.Order
import com.example.pizzaproject.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface FirestoreDatasource {

    suspend fun getProducts() : List<Product>

    suspend fun addClient(user: Client)

    suspend fun sendOrder(
        order: Order,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
        )

}