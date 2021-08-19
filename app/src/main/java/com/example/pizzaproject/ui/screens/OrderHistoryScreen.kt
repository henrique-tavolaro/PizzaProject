package com.example.pizzaproject.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pizzaproject.domain.models.Client
import com.example.pizzaproject.domain.models.Order
import com.example.pizzaproject.ui.OrdersViewModel
import com.example.pizzaproject.ui.composables.DrawerContent
import com.example.pizzaproject.ui.composables.OrderHistoryLazyColumnItem
import com.example.pizzaproject.ui.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OrderHistoryScreen(
    loggedUser: MutableState<Client?>,
    orderList: List<Order>?,
    navController: NavController,
    coroutineScope: CoroutineScope,
    total: Double,
    viewModel: OrdersViewModel,
    scaffoldState: ScaffoldState,
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Histórico de Pedidos") },
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "drawer menu icon")
                    }
                },
                actions = {
                    Card(
                        modifier = Modifier.padding(4.dp),
                        shape = RoundedCornerShape(10.dp),
                        onClick = {
                            navController.navigate(Screen.CartScreen.route)
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ShoppingCart,
                                tint = Color.Black,
                                contentDescription = "shopping cart"
                            )
                            Text(
                                "R$ ${total}0",
                                modifier = Modifier.padding(end = 4.dp)
                            )
                        }
                    }
                }
            )

        },
        drawerContent = {
            DrawerContent(
                loggedUser = loggedUser,
                navController = navController,
                scaffoldState = scaffoldState,
                coroutineScope = coroutineScope
            )
        },
    ) {
        LazyColumn() {
            item(){
            }
            items(items = orderList!!){
                OrderHistoryLazyColumnItem(order = it)
            }
        }
    }
}