package com.example.pizzaproject.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizzaproject.Categories
import com.example.pizzaproject.domain.interactors.AddProductToOrder
import com.example.pizzaproject.domain.interactors.GetProducts
import com.example.pizzaproject.domain.interactors.GetOrderTotal
import com.example.pizzaproject.domain.models.OrderInProgress
import com.example.pizzaproject.domain.models.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.pizzaproject.domain.interactors.GetCart
import com.example.pizzaproject.domain.models.CartDetail

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getProducts: GetProducts,
    private val addProductToOrder: AddProductToOrder,
    private val getOrderTotal: GetOrderTotal,
    private val getCart: GetCart
) : ViewModel() {

    val loading = mutableStateOf(false)

    val productsList = mutableStateOf<List<Product>>(listOf())

    val stickyHeaderIndex1 = mutableStateOf(0)

    val stickyHeaderIndex2 = mutableStateOf(0)

    val categorySelected = mutableStateOf(Categories.PIZZAS)

    val bottomBarVisibility = mutableStateOf(false)

    val isCartOpen = mutableStateOf(false)

    init {
        getProductList()
        getTotalSum()
        getCart()
    }

    private fun getProductList() {
        viewModelScope.launch {
            getProducts.execute().onEach { dataState ->
                loading.value = dataState.loading

                dataState.data?.let {
                    productsList.value = it
                    getStickyHeadersIndex(it)
                    Log.d("TAG", it.toString())
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

    fun addProductToOrder(orderInProgress: OrderInProgress) {
        viewModelScope.launch {
            addProductToOrder.execute(orderInProgress)
        }
    }

    val totalSum = mutableStateOf(0.0)

    fun getTotalSum() {
        viewModelScope.launch {
            getOrderTotal.execute().collect {
                if (it != null) {
                    totalSum.value = it
                }
            }
        }
    }

    val cart = mutableStateOf<List<CartDetail>>(listOf())

    fun getCart() {
        viewModelScope.launch {
            getCart.execute().collect {
                if (it != null) {
                    cart.value = it
                }
            }
        }
    }
}