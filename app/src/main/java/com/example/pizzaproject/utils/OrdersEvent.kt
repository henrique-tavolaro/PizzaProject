package com.example.pizzaproject.utils

import android.content.Context
import com.example.pizzaproject.domain.models.Client
import com.example.pizzaproject.domain.models.Order
import com.example.pizzaproject.domain.models.OrderInProgress
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

sealed class OrdersEvent {

    object GetProductListEvent : OrdersEvent()

    object GetTotalSumEvent : OrdersEvent()

    object GetCartEvent : OrdersEvent()

    data class AddProductToOrderEvent(val orderInProgress: OrderInProgress) : OrdersEvent()

    object ClearCartEvent : OrdersEvent()

    data class DeleteProductFromOrderEvent(val product: String) : OrdersEvent()

    data class HandleSignInResultEvent(
        val task: Task<GoogleSignInAccount>,
        val context: Context
    ) : OrdersEvent()

    data class SendOrderEvent(
        val order: Order,
        val onSuccess: () -> Unit,
        val onFailure: () -> Unit
    ): OrdersEvent()

    data class GetOrderListEvent(val clientId: String): OrdersEvent()

}
