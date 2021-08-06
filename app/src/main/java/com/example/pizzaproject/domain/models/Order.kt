package com.example.pizzaproject.domain.models

data class Order(

    val id: String = "",
    val date: String = "",
    val address: String = "",
    val details: List<OrderLine> = listOf(),
    val totalPrice: String = "",
    val paymentMethod: String = ""

)

data class OrderLine(

    val name: String = "",
    val quantity: String = ""

)