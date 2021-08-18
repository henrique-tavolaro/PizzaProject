package com.example.pizzaproject.datasource.firestore

import android.util.Log
import com.example.pizzaproject.domain.models.Client
import com.example.pizzaproject.domain.models.Order
import com.example.pizzaproject.domain.models.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val PRODUCTS = "Product"
const val CLIENTS = "Clients"
const val ORDERS = "Orders"

@ExperimentalCoroutinesApi
class FirestoreDatasourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FirestoreDatasource {

    override suspend fun getProducts(): List<Product> {
       return firestore
           .collection(PRODUCTS)
           .orderBy("categoryOrder")
           .get()
           .await()
           .toObjects(Product::class.java)
    }



    override suspend fun addClient(client: Client) {
        firestore.collection(CLIENTS)
            .document(client.id)
            .set(client, SetOptions.merge())
    }

    override suspend fun sendOrder(
        order: Order,
        onSuccess: () -> Unit,
        onFailure: () -> Unit) {
        firestore
            .collection(ORDERS)
            .document(order.id)
            .set(order, SetOptions.merge())
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure()
            }
    }

    override suspend fun getOrders(clientId: String): List<Order> {
        return firestore
            .collection(ORDERS)
            .whereEqualTo("clientId", clientId)
            .get()
            .await()
            .toObjects(Order::class.java)
    }

}