package com.example.pizzaproject.domain.interactors

import com.example.pizzaproject.datasource.firestore.FakeFirestoreDatasource
import com.example.pizzaproject.datasource.firestore.client1
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AddClientTest {

    lateinit var firestore: FakeFirestoreDatasource

    lateinit var addClient: AddClient

    @Before
    fun setup(){
        firestore = FakeFirestoreDatasource()
        addClient = AddClient(firestore)
    }

    @Test
    fun `should add new client`() = runBlocking {
        assert(firestore.clientList.isEmpty())

        addClient.execute(client1)

        assert(firestore.clientList.isNotEmpty())
        assert(firestore.clientList.contains(client1))
    }


}