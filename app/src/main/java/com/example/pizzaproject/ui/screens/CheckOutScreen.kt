package com.example.pizzaproject.ui.screens

import android.content.Context
import android.opengl.Visibility
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pizzaproject.domain.models.CartDetail
import com.example.pizzaproject.domain.models.Client
import com.example.pizzaproject.domain.models.Order
import com.example.pizzaproject.ui.OrdersViewModel
import com.example.pizzaproject.ui.composables.DrawerContent
import com.example.pizzaproject.ui.composables.SimpleRadioButtonComponent
import com.example.pizzaproject.ui.navigation.Screen
import com.example.pizzaproject.ui.theme.StickHeaderColor
import com.example.pizzaproject.utils.OrderStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CheckOutScreen(
    viewModel: OrdersViewModel,
    total: Double,
    coroutineScope: CoroutineScope,
    navController: NavController,
    loggedUser: MutableState<Client?>,
    context: Context,
    cart: List<CartDetail>
) {
    val scaffoldState = rememberScaffoldState()

    val observationTextField = viewModel.observationTextField.value
    val address = viewModel.addressTextField.value
    val radioOptions = viewModel.radioOptions
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }


    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text("Enviar Pedido")
                },
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
        floatingActionButton = {
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .padding(bottom = 16.dp, end = 16.dp),
                text = { Text("Enviar pedido") },
                onClick = {
                    if (address.isNotEmpty()) {
                        val order = Order(
                            id = SimpleDateFormat("yyMMddHHmmssZ").format(Date()),
                            clientId = loggedUser.value!!.id,
                            clientName = loggedUser.value!!.name,
                            observation = observationTextField,
                            date = SimpleDateFormat("dd/MM/yyyy").format(
                                Date()
                            ),
                            address = address,
                            details = cart,
                            totalPrice = total,
                            paymentMethod = selectedOption,
                            status = OrderStatus.OPEN
                        )
                        viewModel.sendOrder(
                            order = order,
                            onSuccess = {
                                viewModel.clearCart()
                                navController.popBackStack()
                                coroutineScope.launch {
                                    scaffoldState
                                        .snackbarHostState
                                        .showSnackbar("Pedido enviado")
                                }
                            },
                            onFailure = {
                                Toast.makeText(
                                    context,
                                    "Erro ao enviar o pedido",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        )
                    } else {
                        Toast.makeText(
                            context,
                            "Insira o endereço para enviar o pedido",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })

        }
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            elevation = 4.dp
        ) {
            Column() {
                Text(
                    text = "Forma de pagamento:",
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
                SimpleRadioButtonComponent(
                    radioOptions,
                    selectedOption,
                    onOptionSelected
                )
                Text(
                    text = "Endereço:",
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
                OutlinedTextField(
                    modifier = Modifier
                        .padding(top = 8.dp, start = 8.dp, bottom = 16.dp, end = 16.dp)
                        .fillMaxWidth(),
                    value = address,
                    onValueChange = viewModel::onAddressChange,
                    label = {
                        Text("Endereço")
                    }
                )
                Text(
                    text = "Observações do pedido:",
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
                OutlinedTextField(
                    modifier = Modifier
                        .padding(top = 8.dp, start = 8.dp, bottom = 16.dp, end = 16.dp)
                        .fillMaxWidth(),
                    value = observationTextField,
                    onValueChange = viewModel::onObservationChange,
                    label = {
                        Text("Observação")
                    }
                )
                Text(
                    text = "Total do pedido: R$ ${total}0",
                    modifier = Modifier.padding(8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }
        }
    }


}

