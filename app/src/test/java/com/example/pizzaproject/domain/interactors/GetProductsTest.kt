package com.example.pizzaproject.domain.interactors

import com.example.pizzaproject.datasource.firestore.FakeFirestoreDatasource
import com.example.pizzaproject.datasource.firestore.product1
import com.example.pizzaproject.datasource.firestore.product2
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class GetProductsTest {

    lateinit var firestore: FakeFirestoreDatasource

    lateinit var getProducts: GetProducts

    @Before
    fun setup(){
        firestore = FakeFirestoreDatasource()
        getProducts = GetProducts(firestore)
    }

    @Test
    fun `get products list from firestore`() = runBlocking {
        assert(firestore.productList.isEmpty())

        firestore.addProducts(product1, product2)

        val result = getProducts.execute().toList()

        assert(result[0].loading)

        val product = result[1].data
        assert(product!!.isNotEmpty())
        assert(product.contains(product1))
        assert(product.contains(product2))

        assert(!result[1].loading)

    }
}