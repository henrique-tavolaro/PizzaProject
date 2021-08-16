package com.example.pizzaproject.domain.models

data class Order(

    val id: String = "",
    val date: String = "",
    val address: String = "",
    val details: List<CartDetail> = listOf(),
    val totalPrice: String = "",
    val paymentMethod: String = "",
    val status: String = ""

)

