package com.example.pizzaproject.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizzaproject.domain.interactors.*
import com.example.pizzaproject.domain.models.*
import com.example.pizzaproject.utils.Categories
import com.example.pizzaproject.utils.OrdersEvent
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getProducts: GetProducts,
    private val addProductToOrder: AddProductToOrder,
    private val getOrderTotal: GetOrderTotal,
    private val getCart: GetCart,
    private val clearCart: ClearCart,
    private val deleteProductFromOrder: DeleteProductFromOrder,
    private val addClient: AddClient,
    private val sendOrder: SendOrder,
    private val getOrders: GetOrders
) : ViewModel() {

    val loading = mutableStateOf(false)

    val productsList = mutableStateOf<List<Product>>(listOf())

    val stickyHeaderIndex1 = mutableStateOf(0)

    val stickyHeaderIndex2 = mutableStateOf(0)

    val categorySelected = mutableStateOf(Categories.PIZZAS)

    val bottomBarVisibility = mutableStateOf(false)

    val googleButtonVisibility = mutableStateOf(false)

    val loggedUser: MutableState<Client?> = mutableStateOf(null)

    val radioOptions = listOf("Cartão", "Dinheiro")

    val hasOpenOrder = mutableStateOf(false)

    val totalSum = mutableStateOf(0.0)

    val cart = mutableStateOf<List<CartDetail>>(listOf())

    val orderList : MutableState<List<Order>> = mutableStateOf(listOf())

    val addressTextField = mutableStateOf("")

    fun onAddressChange(text: String) {
        addressTextField.value = text
    }

    val observationTextField = mutableStateOf("")

    fun onObservationChange(text: String) {
        observationTextField.value = text
    }

    init {
        onTriggerEvent(OrdersEvent.GetTotalSumEvent)
        onTriggerEvent(OrdersEvent.GetCartEvent)
        onTriggerEvent(OrdersEvent.GetProductListEvent)
    }

    fun onTriggerEvent(event: OrdersEvent){
        viewModelScope.launch {
            try {
                when(event){
                    is OrdersEvent.GetCartEvent -> {
                        getCart()
                    }
                    is OrdersEvent.GetProductListEvent -> {
                        getProductList()
                    }
                    is OrdersEvent.GetTotalSumEvent -> {
                        getTotalSum()
                    }
                    is OrdersEvent.SendOrderEvent -> {
                        sendOrder(event.order, event.onSuccess, event.onFailure)
                    }
                    is OrdersEvent.GetOrderListEvent -> {
                        getOrderList(event.clientId)
                    }
                    is OrdersEvent.AddProductToOrderEvent -> {
                        addProductToOrder(event.orderInProgress)
                    }
                    is OrdersEvent.DeleteProductFromOrderEvent -> {
                        deleteProductFromOrder(event.product)
                    }
                    is OrdersEvent.ClearCartEvent -> {
                        clearCart()
                    }
                    is OrdersEvent.HandleSignInResultEvent -> {
                        handleSignInResult(event.task, event.context)
                    }

                }
            } catch (e: Exception){
                Log.e("TAG", "launchJob: Exception: ${e}, ${e.cause}")
                e.printStackTrace()
            }
            finally {
                Log.d("TAG", "launchJob: finally called.")
            }
        }
    }


    private fun getProductList() {
        viewModelScope.launch {
            getProducts.execute().onEach { dataState ->
                loading.value = dataState.loading

                dataState.data?.let {
                    productsList.value = it
                    getStickyHeadersIndex(it)
                }

                dataState.error?.let {
                    // TODO handle error
                    Log.d("TAG", it.toString())
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun getStickyHeadersIndex(list: List<Product>) {
        for (i in list) {
            if (i.categoryOrder == 1) {
                stickyHeaderIndex1.value++
                stickyHeaderIndex2.value++
            } else if (i.categoryOrder == 2)
                stickyHeaderIndex2.value++
        }
    }

    private fun addProductToOrder(orderInProgress: OrderInProgress) {
        viewModelScope.launch {
            addProductToOrder.execute(orderInProgress)
        }
    }


    private fun getTotalSum() {
        viewModelScope.launch {
            getOrderTotal.execute().collect {
                if (it != null) {
                    totalSum.value = it
                } else {
                    totalSum.value = 0.0
                }
            }
        }
    }

    private fun getCart() {
        viewModelScope.launch {
            getCart.execute().collect {
                if (it != null) {
                    cart.value = it
                }
            }
        }
    }

    private fun clearCart() {
        viewModelScope.launch {
            clearCart.execute()
        }
    }

    private fun deleteProductFromOrder(product: String) {
        viewModelScope.launch {
            deleteProductFromOrder.execute(product)
        }
    }

    private fun addClient(client: Client) {
        viewModelScope.launch {
            addClient.execute(client)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>, context: Context) {
        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)!!

            val client = Client(
                account.id!!,
                account.displayName!!,
                account.photoUrl!!.toString(),
                account.email!!
            )
            loggedUser.value = client
            addClient(client)
        } catch (e: ApiException) {
            Toast.makeText(context, "Falha ao fazer login", Toast.LENGTH_LONG).show()
        }
    }

    private fun sendOrder(order: Order,
                  onSuccess: () -> Unit,
                  onFailure: () -> Unit){
        viewModelScope.launch {
            sendOrder.execute(order, onSuccess, onFailure)

        }
    }

    private fun getOrderList(clientId: String) {
        viewModelScope.launch {
            getOrders.execute(clientId).collect{
                orderList.value = it!!
            }
        }

    }

}