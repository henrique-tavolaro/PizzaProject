package com.example.pizzaproject.domain.interactors

import com.example.pizzaproject.datasource.firestore.FirestoreDatasource
import com.example.pizzaproject.domain.models.Client
import com.google.firebase.firestore.FirebaseFirestore

class AddClient(
    private val firestore: FirestoreDatasource
) {

    suspend fun execute(client: Client){
        firestore.addClient(client)
    }

}