package com.example.pizzaproject.domain.interactors

import com.example.pizzaproject.datasource.firestore.FirestoreDatasource
import com.example.pizzaproject.domain.models.Order

class SendOrder(
    private val firestore: FirestoreDatasource
) {

    suspend fun execute(
        order: Order,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ){
        firestore.sendOrder(order, onSuccess, onFailure)
    }
}