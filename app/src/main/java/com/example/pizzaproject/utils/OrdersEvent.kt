package com.example.pizzaproject.utils

sealed class OrdersEvent {

    object GetProductListEvent : OrdersEvent()

    object GetTotalSumEvent : OrdersEvent()

    object GetCartEvent : OrdersEvent()

}
