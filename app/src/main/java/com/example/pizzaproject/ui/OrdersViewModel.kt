package com.example.pizzaproject.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pizzaproject.Categories
import com.example.pizzaproject.domain.interactors.GetProducts
import com.example.pizzaproject.domain.models.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getProducts: GetProducts
): ViewModel() {

    val loading = mutableStateOf(false)

    val productsList  = mutableStateOf<List<Product>>(listOf())

    val stickyHeaderIndex1 = mutableStateOf(0)

    val stickyHeaderIndex2 = mutableStateOf(0)

    val categorySelected = mutableStateOf(Categories.PIZZAS)

    init {
        getProductList()
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

    private fun getStickyHeadersIndex(list: List<Product>){
        for(i in list){
            if(i.categoryOrder == 1){
                stickyHeaderIndex1.value++
                stickyHeaderIndex2.value++
            } else if(i.categoryOrder == 2)
                stickyHeaderIndex2.value++
        }
    }

}