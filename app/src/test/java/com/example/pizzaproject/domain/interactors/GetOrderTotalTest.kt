package com.example.pizzaproject.domain.interactors

import com.example.pizzaproject.datasource.room.FakeOrderDao
import com.example.pizzaproject.datasource.room.productInOrder1
import com.example.pizzaproject.datasource.room.productInOrder2
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

internal class GetOrderTotalTest {

    lateinit var dao: FakeOrderDao

    lateinit var getOrderTotal: GetOrderTotal

    @Before
    fun setup(){
        dao = FakeOrderDao()
        getOrderTotal = GetOrderTotal(dao)
    }

    @Test
    fun `should emit the sum of prices on the cart`() = runBlocking{
        assert(dao.orderInProgressList.isEmpty())

        var total = 0.0

        //assert when list is empty value is 0.0
        dao.getOrderTotal().collect {
            if(it != null){
                total = it
            }
        }
        assert(total.equals(0.0))

        dao.addProductToOrder(productInOrder1)
        dao.addProductToOrder(productInOrder2)

        //assert the total is sum of price productInOrder1(55.0) and productInOrder2(60.0)
        getOrderTotal.execute().collect {
            if(it != null){
                total = it
            }
        }

        val sum = productInOrder1.price + productInOrder2.price

        assert(total.equals(sum))
    }

}