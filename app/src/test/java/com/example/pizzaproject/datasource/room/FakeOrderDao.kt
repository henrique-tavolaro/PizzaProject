package com.example.pizzaproject.datasource.room

import com.example.pizzaproject.domain.models.CartDetail
import com.example.pizzaproject.domain.models.OrderInProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

val productInOrder1 = OrderInProgress(1, "Atum", 55.0)
val productInOrder2 = OrderInProgress(2, "Portuguesa", 60.0)
val productInOrder3 = OrderInProgress(3, "Calabresa", 70.0)
val productInOrder4 = OrderInProgress(4, "Mussarela", 50.0)

class FakeOrderDao(
    var orderInProgressList: MutableList<OrderInProgress> = mutableListOf()
): OrderDao {

    fun getOrder() : List<OrderInProgress>{
        return orderInProgressList
    }

    override suspend fun addProductToOrder(orderInProgress: OrderInProgress) {
        orderInProgressList.add(orderInProgress)
    }

    override fun getOrderTotal(): Flow<Double?> = flow {
        var orderTotal = 0.0
        if(orderInProgressList.isNotEmpty()){
            for(i in orderInProgressList){
                orderTotal += i.price
            }
            emit(orderTotal)
        } else {
            emit(null)
        }

    }

    override suspend fun deleteProductFromOrder(product: String) {
        if (orderInProgressList.isNotEmpty()) {
            for (i in orderInProgressList) {
                if (i.product == product)
                    orderInProgressList.remove(i)
            }
        }
    }

    override suspend fun clearCart() {
        orderInProgressList.clear()
    }

    override fun getCart(): Flow<List<CartDetail>?> = flow {

            if(orderInProgressList.isNotEmpty()){
            val cartDetailList = orderInProgressList
            .groupBy { it.product }
            .map { CartDetail(it.key, it.value.sumOf { orderInProgress ->
                orderInProgress.price }, it.value.size) }
                emit(cartDetailList)
            }

        }
    }


