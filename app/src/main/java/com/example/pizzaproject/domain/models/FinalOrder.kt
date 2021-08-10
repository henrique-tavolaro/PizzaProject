package com.example.pizzaproject.domain.models

import com.google.type.DateTime
import com.google.type.PostalAddress

data class FinalOrder(

    val id: String = "",
    val date: String = "",
    val address: String = "",
    val observation: String = "",
    val paymentMethod: String = "",
    val order: List<OrderInProgress> = listOf(),

)
