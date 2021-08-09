package com.example.pizzaproject.domain.interactors

import com.example.pizzaproject.datasource.room.FakeOrderDao
import com.example.pizzaproject.datasource.room.productInOrder1
import com.example.pizzaproject.datasource.room.productInOrder2
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class AddProductToOrderTest {

    lateinit var dao: FakeOrderDao

    lateinit var addProductToOrder: AddProductToOrder

    @Before
    fun setup(){
        dao = FakeOrderDao()
        addProductToOrder = AddProductToOrder(dao)
    }

    @Test
    fun `should add product to order list`() = runBlocking{
        assert(dao.orderInProgressList.isEmpty())

        addProductToOrder.execute(productInOrder1)
        addProductToOrder.execute(productInOrder2)

        assert(dao.orderInProgressList.isNotEmpty())
        assert(dao.orderInProgressList.contains(productInOrder1))
        assert(dao.orderInProgressList.contains(productInOrder2))
    }

}