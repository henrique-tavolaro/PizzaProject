package com.example.pizzaproject.utils

import com.example.pizzaproject.R


object Images {
    const val image1 = R.drawable.pizza
    const val image2 = R.drawable.pizza2
    const val image3 = R.drawable.pizza3
}

object Categories {
    const val PIZZAS = "Pizzas"
    const val DESSERT = "Sobremesas"
    const val DRINKS = "Bebidas"
}

object OrderStatus {
    const val OPEN = "Aberto"
    const val ACCEPTED = "Preparando pedido"
    const val CANCELLEDBYCLIENT = "Cancelado pelo cliente"
    const val CANCELLEDBYRESTAURANT = "Cancelado pelo restaurante"
    const val DELIVERED = "Entregue"

}

const val DEFAULT_IMAGE = R.drawable.ic_user_place_holder