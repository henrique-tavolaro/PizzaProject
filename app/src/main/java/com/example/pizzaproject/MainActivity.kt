package com.example.pizzaproject


import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import com.example.pizzaproject.domain.models.CartDetail
import com.example.pizzaproject.domain.models.Product
import com.example.pizzaproject.ui.OrdersViewModel
import com.example.pizzaproject.ui.navigation.Screen
import com.example.pizzaproject.ui.screens.CartScreen
import com.example.pizzaproject.ui.screens.CheckOutScreen
import com.example.pizzaproject.ui.screens.HomeScreen
import com.example.pizzaproject.ui.theme.PizzaProjectTheme
import com.google.accompanist.navigation.animation.AnimatedComposeNavigator
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: OrdersViewModel by viewModels()

    @SuppressLint("UnusedCrossfadeTargetStateParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberAnimatedNavController()
            val categorySelected = viewModel.categorySelected
            val scrollState = rememberLazyListState()
            val coroutineScope = rememberCoroutineScope()
            val products = viewModel.productsList.value
            val stickyHeaderIndex1 = viewModel.stickyHeaderIndex1.value
            val stickyHeaderIndex2 = viewModel.stickyHeaderIndex2.value
            val loading = viewModel.loading.value
            val getTotal = viewModel.totalSum.value
            val bottomBarVisibility = viewModel.bottomBarVisibility
            val isCartOpen = viewModel.isCartOpen
            val cart = viewModel.cart.value
            val radioOptions = listOf("Cartão", "Dinheiro")
            val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
            val fabVisibility = viewModel.floatingActionButtonVisibility
            val address = viewModel.addressTextField.value

            PizzaProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Log.d("TAG2", cart.toString())
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text("Mario & Luigi") },
                                actions = {
                                    Card(
                                        modifier = Modifier.padding(4.dp),
                                        shape = RoundedCornerShape(10.dp),
                                        onClick = {
                                            if (!isCartOpen.value) {
                                                navController.navigate(Screen.CartScreen.route)
                                                isCartOpen.value = !isCartOpen.value
                                            } else {
                                                navController.popBackStack()
                                                isCartOpen.value = !isCartOpen.value
                                            }
                                        }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.ShoppingCart,
                                                tint = Black,
                                                contentDescription = "shopping cart"
                                            )
                                            Text(
                                                "R$ ${getTotal}0",
                                                modifier = Modifier.padding(end = 4.dp)
                                            )
                                        }
                                    }
                                }
                            )
                        },
                        bottomBar = {
                            AnimatedVisibility(
                                visibleState = MutableTransitionState(bottomBarVisibility.value),
                                enter = slideInVertically(
                                    initialOffsetY = { -60 },
                                    animationSpec = tween(500, easing = LinearEasing)
                                ),
                                exit = slideOutVertically(
                                    targetOffsetY = { -60 },
                                    animationSpec = tween(500, easing = LinearEasing)
                                )
                            ) {
                                Button(
                                    onClick = {
                                        navController.navigate(Screen.CartScreen.route)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp)
                                ) {
                                    Text(
                                        text = "Finalizar Pedido",
                                        modifier = Modifier.align(Alignment.CenterVertically),
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        },
                        floatingActionButton = {
                            Crossfade(targetState = fabVisibility.value) {
                                if(it){
                                    ExtendedFloatingActionButton(
                                        modifier = Modifier
                                            .padding(bottom = 16.dp, end = 16.dp),
                                        text = { Text("Enviar pedido") },
                                        onClick = {
                                            if(address.isNotEmpty()){

                                            } else {
                                                Toast.makeText(
                                                    applicationContext,
                                                    "Insira o enderço para enviar o pedido",
                                                    Toast.LENGTH_LONG).show()
                                            }
                                        })
                                }
                            }
                        }
                    ) {
                        AnimatedNavHost(
                            navController = navController,
                            startDestination = Screen.HomeScreen.route,
                            builder = {
                                addHomeScreen(
                                    bottomBarVisibility = bottomBarVisibility,
                                    getTotal = getTotal,
                                    categorySelected = categorySelected,
                                    coroutineScope = coroutineScope,
                                    scrollState = scrollState,
                                    stickyHeaderIndex1 = stickyHeaderIndex1,
                                    stickyHeaderIndex2 = stickyHeaderIndex2,
                                    products = products,
                                    viewModel = viewModel,
                                    loading = loading,
                                    fabVisibility = fabVisibility,
                                )
                                addCartScreen(
                                    navController = navController,
                                    cart = cart,
                                    bottomBarVisibility = bottomBarVisibility,
                                    total = getTotal,
                                    isCartOpen = isCartOpen,
                                    viewModel = viewModel,
                                    fabVisibility = fabVisibility,
                                )
                                addPaymentScreen(
                                    radioOptions = radioOptions,
                                    selectedOption = selectedOption,
                                    onOptionSelected = onOptionSelected,
                                    viewModel = viewModel,
                                    total = getTotal,
                                    address = address,
                                    fabVisibility = fabVisibility,
                                )
                            })
                    }
                }
            }
        }
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.addPaymentScreen(
    radioOptions: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    viewModel: OrdersViewModel,
    total: Double,
    address: String,
    fabVisibility: MutableState<Boolean>
) {
    composable(
        route = Screen.CheckOutScreen.route,
        enterTransition = { _, _ ->
            slideInHorizontally(
                initialOffsetX = { -600 },
                animationSpec = tween(300, easing = FastOutLinearInEasing)
            )

        }
    ) {
        CheckOutScreen(
            radioOptions = radioOptions,
            selectedOption = selectedOption,
            onOptionSelected = onOptionSelected,
            viewModel = viewModel,
            total = total,
            address = address,
            fabVisibility = fabVisibility
        )
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.addCartScreen(
    bottomBarVisibility: MutableState<Boolean>,
    cart: List<CartDetail>,
    navController: NavController,
    total: Double,
    isCartOpen: MutableState<Boolean>,
    viewModel: OrdersViewModel,
    fabVisibility: MutableState<Boolean>
) {
    composable(
        route = Screen.CartScreen.route,
        enterTransition = { _, _ ->
            slideInVertically(
                initialOffsetY = { -3000 },
                animationSpec = tween(300, easing = LinearEasing)
            )
        },
        exitTransition = { _, _ ->
            shrinkOut(

                animationSpec = tween(300, easing = LinearOutSlowInEasing)
            )
        },
        popExitTransition = { _, _ ->
            slideOutVertically(
                targetOffsetY = { -3000 },
                animationSpec = tween(300, easing = LinearEasing)
            )
        },

        ) {
        CartScreen(
            navController = navController,
            bottomBarVisibility = bottomBarVisibility,
            cart = cart,
            total = total,
            isCartOpen = isCartOpen,
            viewModel = viewModel,
            fabVisibility = fabVisibility
        )
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.addHomeScreen(
    bottomBarVisibility: MutableState<Boolean>,
    getTotal: Double,
    categorySelected: MutableState<String>,
    coroutineScope: CoroutineScope,
    scrollState: LazyListState,
    stickyHeaderIndex1: Int,
    stickyHeaderIndex2: Int,
    products: List<Product>,
    viewModel: OrdersViewModel,
    loading: Boolean,
    fabVisibility: MutableState<Boolean>
) {
    composable(
        route = Screen.HomeScreen.route,
        exitTransition = { _, _ ->
            fadeOut(animationSpec = tween(500))
        },
        popEnterTransition = { _, _ ->
            fadeIn(animationSpec = tween(500))
        }
    ) {
        HomeScreen(
            bottomBarVisibility = bottomBarVisibility,
            getTotal = getTotal,
            categorySelected = categorySelected,
            coroutineScope = coroutineScope,
            scrollState = scrollState,
            stickyHeaderIndex1 = stickyHeaderIndex1,
            stickyHeaderIndex2 = stickyHeaderIndex2,
            products = products,
            viewModel = viewModel,
            loading = loading,
            fabVisibility = fabVisibility
        )
    }
}


@ExperimentalAnimationApi
@Composable
public fun rememberAnimatedNavController(): NavHostController {
    val navController = rememberNavController()
    val animatedNavigator = remember(navController) { AnimatedComposeNavigator() }
    return navController.apply {
        navigatorProvider += animatedNavigator
    }
}

object Images {
    const val image1 = R.drawable.pizza
    const val image2 = R.drawable.pizza2
    const val image3 = R.drawable.pizza3
}

object Categories {
    const val PIZZAS = "Pizzas"
    const val DESSERT = "Dessert"
    const val DRINKS = "Drinks"
}
