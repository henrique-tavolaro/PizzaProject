package com.example.pizzaproject.ui.screens

import android.content.Context
import androidx.compose.material.Text
import android.content.Intent
import android.os.Handler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.example.pizzaproject.domain.models.Client
import com.example.pizzaproject.domain.models.Order
import com.example.pizzaproject.ui.OrdersViewModel
import com.example.pizzaproject.ui.composables.GoogleButton
import com.example.pizzaproject.ui.navigation.Screen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.flow.collect

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun SplashSignInScreen(
    onClick: () -> Unit,
    googleButtonVisibility: MutableState<Boolean>,
    navController: NavController,
    context: Context,
    loggedUser: MutableState<Client?>,
    viewModel: OrdersViewModel
){

    val handler = Handler()
    handler.postDelayed({
        val account = GoogleSignIn.getLastSignedInAccount(context)
        if (account != null) {
            val client = Client(
                id = account.id!!,
                name = account.displayName!!,
                email = account.email!!,
                photoUrl = account.photoUrl!!.toString()
            )
            loggedUser.value = client
            navController.navigate(Screen.HomeScreen.route)
            viewModel.getOrderList(loggedUser.value!!.id)
        } else {
            googleButtonVisibility.value = true
        }
    }, 0)

    if(loggedUser.value != null){
        navController.navigate(Screen.HomeScreen.route)
        viewModel.getOrderList(loggedUser.value!!.id)
    }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primaryVariant),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.3f))
            Text("Mario & Luigi Pizzaria", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.fillMaxHeight(0.4f))
            AnimatedVisibility(visible = googleButtonVisibility.value) {
                GoogleButton(
                    onClick = onClick
                )

            }

        }
    }





