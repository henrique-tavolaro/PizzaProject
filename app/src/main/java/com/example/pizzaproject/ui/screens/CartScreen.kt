package com.example.pizzaproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.rounded.Cake
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.sharp.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pizzaproject.domain.models.CartDetail
import com.example.pizzaproject.ui.OrdersViewModel
import com.example.pizzaproject.ui.navigation.Screen
import com.example.pizzaproject.ui.theme.StickHeaderColor

@Composable
fun CartScreen(
    navController: NavController,
    bottomBarVisibility: MutableState<Boolean>,
    cart: List<CartDetail>,
    total: Double,
    isCartOpen: MutableState<Boolean>,
    viewModel: OrdersViewModel,
    fabVisibility: MutableState<Boolean>
) {
    bottomBarVisibility.value = false
    fabVisibility.value = false

    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Esvaziar carrinho")
            IconButton(
               onClick = {
                    viewModel.clearCart()

            }
                ){
                Icon(Icons.Filled.Delete, contentDescription = "Esvaziar carrinho")
            }
        }
        Text(
            text = "Resumo do pedido:",
            fontSize = 22.sp,
            fontStyle = FontStyle.Italic,
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
                .fillMaxWidth(),
            elevation = 4.dp
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                items(items = cart) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(text = "${it.productCount}x   ${it.product}")
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = "R$ ${it.sumPrice}0",
                                fontWeight = FontWeight.Bold,
                            )
                            IconButton(
                                onClick = {
                                    viewModel.deleteProductFromOrder(it.product)
                                }) {
                                Icon(Icons.Filled.Clear, contentDescription = "Delete item icon")
                            }
                        }

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
                enabled = total != 0.0,
                onClick = {
                    navController.navigate(Screen.CheckOutScreen.route){
                        isCartOpen.value = !isCartOpen.value
                        popUpTo(Screen.HomeScreen.route)
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