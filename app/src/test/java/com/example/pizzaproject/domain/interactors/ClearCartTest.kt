package com.example.pizzaproject.domain.interactors

import com.example.pizzaproject.datasource.room.FakeOrderDao
import com.example.pizzaproject.datasource.room.productInOrder1
import com.example.pizzaproject.datasource.room.productInOrder2
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ClearCartTest{

    lateinit var dao: FakeOrderDao

    lateinit var clearCart: ClearCart

    @Before
    fun setup(){
        dao = FakeOrderDao()
        clearCart = ClearCart(dao)
    }

    @Test
    fun `should empty the cart`() = runBlocking {
        dao.addProductToOrder(productInOrder1)
        dao.addProductToOrder(productInOrder1)
        dao.addProductToOrder(productInOrder2)

        assert(dao.orderInProgressList.isNotEmpty())

        clearCart.execute()
        assert(dao.orderInProgressList.isEmpty())
    }

}