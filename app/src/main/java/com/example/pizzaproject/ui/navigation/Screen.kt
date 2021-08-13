package com.example.pizzaproject.ui.navigation

sealed class Screen(val route: String){

    object SplashSignInScreen: Screen(route = "splashSignInScreen")

    object HomeScreen: Screen(route = "homeScreen")

    object CartScreen: Screen(route = "cartScreen")

    object CheckOutScreen: Screen(route = "checkoutScreen")

}
