package com.example.pizzaproject.domain.interactors

import com.example.pizzaproject.datasource.room.FakeOrderDao
import com.example.pizzaproject.datasource.room.OrderDao
import com.example.pizzaproject.datasource.room.productInOrder1
import com.example.pizzaproject.datasource.room.productInOrder2
import com.example.pizzaproject.domain.models.CartDetail
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetCartTest {

    lateinit var dao: FakeOrderDao

    lateinit var getCart: GetCart

    @Before
    fun setup(){
        dao = FakeOrderDao()
        getCart = GetCart(dao)
    }

    @Test
    fun `should return a list of CartDetail`() = runBlocking{
        assert(dao.orderInProgressList.isEmpty())

        dao.addProductToOrder(productInOrder1)
        dao.addProductToOrder(productInOrder1)
        dao.addProductToOrder(productInOrder2)

        val cart = listOf<CartDetail>(
            CartDetail(product = "Atum", productCount = 2, sumPrice = 110.0),
            CartDetail(product = "Portuguesa", productCount = 1, sumPrice = 60.0),
        )

        var newCart = listOf<CartDetail>()

            getCart.execute().collect {
                newCart = it!!
            }

        assert(newCart == cart)
    }

}