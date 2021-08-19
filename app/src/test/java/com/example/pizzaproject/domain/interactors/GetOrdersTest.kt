package com.example.pizzaproject.domain.interactors

import com.example.pizzaproject.datasource.firestore.*
import com.example.pizzaproject.domain.models.Order
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetOrdersTest{

    lateinit var firestore: FakeFirestoreDatasource

    lateinit var getOrders: GetOrders

    @Before
    fun setup(){
        firestore = FakeFirestoreDatasource()
        getOrders = GetOrders(firestore
        )
    }

    @Test
    fun `should emit orders where id equals clientId`()= runBlocking{
        assert(firestore.orderList.isEmpty())

        firestore.addOrders(order1, order2)

        val list = mutableListOf<Order>()

            getOrders.execute("clientId1").collect {
                list.addAll(it!!)
            }

        assert(list.isNotEmpty())


        assert(list.contains(order1))
        assert(list.contains(order2))

    }

}