package com.example.pizzaproject.domain.interactors

import com.example.pizzaproject.datasource.room.FakeOrderDao
import com.example.pizzaproject.datasource.room.OrderDao
import com.example.pizzaproject.datasource.room.productInOrder1
import com.example.pizzaproject.datasource.room.productInOrder2
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DeleteProductFromOrderEventTest {

    lateinit var dao: FakeOrderDao

    lateinit var deleteProductFromOrder: DeleteProductFromOrder

    @Before
    fun setup(){
        dao = FakeOrderDao()
        deleteProductFromOrder = DeleteProductFromOrder(dao)
    }

    //given
    val list1 = listOf(productInOrder1, productInOrder1, productInOrder2)
    val list2 = listOf(productInOrder1, productInOrder2)
    val list3 = listOf(productInOrder2)
    val list4 = listOf(productInOrder1, productInOrder1)

    @Test
    fun `should delete the all of the same product from the cart`() = runBlocking{
        assert(dao.orderInProgressList.isEmpty())
        // TODO GOT TO MAKE THIS WORK
        dao.addProductToOrder(productInOrder1)
        assert(dao.orderInProgressList.isNotEmpty())
        deleteProductFromOrder.execute(productInOrder1.product)
        val list = listOf(productInOrder1)
        assert(dao.orderInProgressList == list)

    }

}