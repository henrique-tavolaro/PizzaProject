package com.example.pizzaproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Payments
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pizzaproject.domain.models.CartDetail
import com.example.pizzaproject.ui.navigation.Screen
import com.example.pizzaproject.ui.theme.StickHeaderColor

@Composable
fun CartScreen(
    navController: NavController,
    bottomBarVisibility: MutableState<Boolean>,
    cart: List<CartDetail>,
    total: Double,
    isCartOpen: MutableState<Boolean>
) {
    bottomBarVisibility.value = false


    Column(
        modifier = Modifier.padding(16.dp),
    ) {
        Text(
            text = "Resumo do pedido:",
            fontSize = 22.sp,
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            StickHeaderColor,
                            Color.Transparent
                        )
                    )
                )
                .padding(8.dp)
                .fillMaxWidth()
        )
        Card(
            modifier = Modifier
                .weight(1f)
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                items(items = cart) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${it.productCount}x   ${it.product}")
                        Text(
                            text = "R$ ${it.sumPrice}0",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "Total do pedido: R$ ${total}0",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Button(
                onClick = {
                    navController.navigate(Screen.CheckOutScreen.route){
                        popUpTo(Screen.HomeScreen.route)
                        isCartOpen.value = !isCartOpen.value
                    }
                },
            ) {
                Text(
                    text = "Ir para pagamento",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Icon(Icons.Filled.Payments, contentDescription = "Payment icon")
                Icon(Icons.Filled.ArrowForward, contentDescription = "Payment icon")
            }
        }
    }
}