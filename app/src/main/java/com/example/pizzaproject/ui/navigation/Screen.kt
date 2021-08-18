package com.example.pizzaproject.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector?, val title: String?){

    object SplashSignInScreen: Screen(route = "splashSignInScreen", null, null)

    object HomeScreen: Screen
        (route = "homeScreen",
        icon = Icons.Filled.Storefront,
        title = "Pedidos")

    object CartScreen: Screen(route = "cartScreen", null, null)

    object CheckOutScreen: Screen(route = "checkoutScreen", null, null)

    object OrderHistoryScreen: Screen(
        route = "orderHistoryScreen",
        icon = Icons.Filled.AccessTime,
        title = "Histórico")

    object ChatScreen: Screen(
        route = "chatScreen",
    icon = Icons.Filled.Chat,
    title = "Contato")

}
