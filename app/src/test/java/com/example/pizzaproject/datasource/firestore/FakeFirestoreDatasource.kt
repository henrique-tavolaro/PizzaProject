package com.example.pizzaproject.datasource.firestore


import com.example.pizzaproject.domain.models.CartDetail
import com.example.pizzaproject.domain.models.Client
import com.example.pizzaproject.domain.models.Order
import com.example.pizzaproject.domain.models.Product
import com.example.pizzaproject.utils.OrderStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

val product1 = Product("id1", "Mussarela", 50.0, "pizza")
val product2 = Product("id2", "Calabresa", 60.0, "pizza")
val client1 = Client("id1", "name", "photo", "email")
val order1 = Order("id1", "clientId1","12/02/2021", "obs","12/10/2021","address",
    listOf(CartDetail("Mussarela", 50.0, 1)),
    50.0, "cartão", OrderStatus.OPEN
    )
val order2 = Order("id2", "clientId2","12/02/2021", "obs","12/10/2021","address",
    listOf(CartDetail("Mussarela", 50.0, 1)),
    50.0, "cartão", OrderStatus.OPEN
    )

class FakeFirestoreDatasource(
    var productList: MutableList<Product> = mutableListOf(),
    var clientList: MutableList<Client> = mutableListOf(),
    var orderList: MutableList<Order> = mutableListOf(),
) : FirestoreDatasource{

    fun addProducts(vararg product: Product){
        productList.addAll(product)

    }

    fun addOrders(vararg order: Order){
        orderList.addAll(order)

    }

    override suspend fun getProducts(): List<Product> {
        productList.add(product1)
        productList.add(product2)
        return productList
    }

    override suspend fun addClient(user: Client) {
        clientList.add(user)
    }

    override suspend fun sendOrder(order: Order, onSuccess: () -> Unit, onFailure: () -> Unit) {
        orderList.add(order)
    }

    override suspend fun getOrders(clientId: String): Flow<List<Order>?> = flow {
        emit(orderList)
    }

//    override suspend fun getOrders(clientId: String): Flow<List<Order>> {
//        return
////        return orderList.filter { it.clientId == clientId }
//    }


}