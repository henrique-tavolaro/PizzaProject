package com.example.pizzaproject.domain.interactors

import com.example.pizzaproject.datasource.firestore.FakeFirestoreDatasource
import com.example.pizzaproject.datasource.firestore.order1
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class SendOrderTest {

    lateinit var firestore: FakeFirestoreDatasource

    lateinit var sendOrder: SendOrder

    @Before
    fun setup(){
        firestore = FakeFirestoreDatasource()
        sendOrder = SendOrder(firestore)
    }

    @Test
    fun `should add order to the list`() = runBlocking{
        assert(firestore.orderList.isEmpty())

        sendOrder.execute(order1, {}, {})

        assert(firestore.orderList.isNotEmpty())
        assert(firestore.orderList.contains(order1))
    }


}