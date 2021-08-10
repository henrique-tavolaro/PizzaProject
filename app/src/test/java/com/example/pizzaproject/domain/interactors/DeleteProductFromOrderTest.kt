package com.example.pizzaproject.domain.interactors

import com.example.pizzaproject.datasource.room.FakeOrderDao
import com.example.pizzaproject.datasource.room.OrderDao
import com.example.pizzaproject.datasource.room.productInOrder1
import com.example.pizzaproject.datasource.room.productInOrder2
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DeleteProductFromOrderTest {

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

        // TODO GOT TO MAKE THIS WORK
        dao.addProductToOrder(productInOrder1)
//        dao.addProductToOrder(productInOrder1)
//        dao.addProductToOrder(productInOrder2)

//        assert(dao.orderInProgressList.equals(list3))

        deleteProductFromOrder.execute("Atum")

//        assert(dao.orderInProgressList.equals(list3))
//        assert(dao.orderInProgressList != list2)
        assert(dao.orderInProgressList.isEmpty())

    }

}