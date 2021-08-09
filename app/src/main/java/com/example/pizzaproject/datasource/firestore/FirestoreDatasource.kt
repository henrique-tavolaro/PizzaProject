package com.example.pizzaproject.datasource.firestore

import com.example.pizzaproject.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface FirestoreDatasource {

    suspend fun getProducts() : List<Product>


}