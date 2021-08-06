package com.example.pizzaproject.datasource.firestore

import com.example.pizzaproject.domain.models.Product

val product1 = Product("id1", "Mussarela", 50.0, "pizza")
val product2 = Product("id2", "Calabresa", 60.0, "pizza")


class FakeFirestoreDatasource(
    var productList: MutableList<Product> = mutableListOf()
) : FirestoreDatasource{

    fun addProducts(vararg product: Product){
        productList.addAll(product)

    }

    override suspend fun getProducts(): List<Product> {
        productList.add(product1)
        productList.add(product2)
        return productList
    }


}