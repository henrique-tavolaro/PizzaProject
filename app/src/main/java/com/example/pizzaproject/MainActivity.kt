package com.example.pizzaproject


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import com.example.pizzaproject.domain.models.CartDetail
import com.example.pizzaproject.domain.models.Client
import com.example.pizzaproject.domain.models.Order
import com.example.pizzaproject.domain.models.Product
import com.example.pizzaproject.ui.OrdersViewModel
import com.example.pizzaproject.ui.navigation.Screen
import com.example.pizzaproject.ui.screens.*
import com.example.pizzaproject.ui.theme.PizzaProjectTheme
import com.google.accompanist.navigation.animation.AnimatedComposeNavigator
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: OrdersViewModel by viewModels()

    @SuppressLint("UnusedCrossfadeTargetStateParameter", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

            val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            val navController = rememberAnimatedNavController()
            val categorySelected = viewModel.categorySelected
            val scrollState = rememberLazyListState()
            val scaffoldState = rememberScaffoldState()
            val coroutineScope = rememberCoroutineScope()
            val products = viewModel.productsList.value
            val loading = viewModel.loading.value
            val total = viewModel.totalSum.value
            val cart = viewModel.cart.value
            val googleButtonVisibility = viewModel.googleButtonVisibility
            val loggedUser = viewModel.loggedUser
            val orderList = viewModel.orderList.value
            val hasOpenOrder = viewModel.hasOpenOrder

            PizzaProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                        AnimatedNavHost(
                            navController = navController,
                            startDestination = Screen.SplashSignInScreen.route,
                            builder = {
                                addSplashSignInScreen(
                                    googleButtonVisibility = googleButtonVisibility,
                                    onClick = {
                                        val signInIntent: Intent =
                                            mGoogleSignInClient.signInIntent
                                        startActivityForResult(signInIntent, RC_SIGN_IN)
                                    },
                                    context = this@MainActivity,
                                    loggedUser = loggedUser,
                                    navController = navController,
                                    viewModel = viewModel
                                )
                                addHomeScreen(
                                    total = total,
                                    categorySelected = categorySelected,
                                    coroutineScope = coroutineScope,
                                    scrollState = scrollState,
                                    products = products,
                                    viewModel = viewModel,
                                    loading = loading,
                                    loggedUser = loggedUser,
                                    scaffoldState = scaffoldState,
                                    navController = navController,
                                    orderList = orderList,
                                    hasOpenOrder = hasOpenOrder
                                )
                                addCartScreen(
                                    navController = navController,
                                    cart = cart,
                                    total = total,
                                    viewModel = viewModel,
                                    coroutineScope = coroutineScope,
                                    loggedUser = loggedUser,
                                )
                                addPaymentScreen(
                                    viewModel = viewModel,
                                    total = total,
                                    coroutineScope = coroutineScope,
                                    navController = navController,
                                    loggedUser = loggedUser,
                                    cart = cart,
                                    context = this@MainActivity,
                                    hasOpenOrder = hasOpenOrder
                                )
                                addOrderHistoryScreen(
                                    loggedUser = loggedUser,
                                    coroutineScope = coroutineScope,
                                    navController = navController,
                                    total = total,
                                    orderList = orderList,
                                    viewModel = viewModel,
                                    scaffoldState = scaffoldState
                                )
                                addChatScreen()
                            })

                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            viewModel.handleSignInResult(task, this)
        }
    }
}


const val RC_SIGN_IN = 123

@ExperimentalAnimationApi
fun NavGraphBuilder.addPaymentScreen(
    viewModel: OrdersViewModel,
    total: Double,
    coroutineScope: CoroutineScope,
    navController: NavController,
    loggedUser: MutableState<Client?>,
    context: Context,
    cart: List<CartDetail>,
    hasOpenOrder: MutableState<Boolean>
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
            viewModel = viewModel,
            total = total,
            coroutineScope = coroutineScope,
            navController = navController,
            loggedUser = loggedUser,
            context = context,
            cart = cart,
            hasOpenOrder = hasOpenOrder
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalAnimationApi
fun NavGraphBuilder.addCartScreen(
    cart: List<CartDetail>,
    navController: NavController,
    total: Double,
    viewModel: OrdersViewModel,
    coroutineScope: CoroutineScope,
    loggedUser: MutableState<Client?>
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
            cart = cart,
            total = total,
            viewModel = viewModel,
            coroutineScope = coroutineScope,
            loggedUser = loggedUser,
        )
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
fun NavGraphBuilder.addSplashSignInScreen(
    googleButtonVisibility: MutableState<Boolean>,
    onClick: () -> Unit,
    context: Context,
    loggedUser: MutableState<Client?>,
    navController: NavController,
    viewModel: OrdersViewModel
) {
    composable(
        route = Screen.SplashSignInScreen.route,
        exitTransition = { _, _ ->
            fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = { _, _ ->
            fadeIn(animationSpec = tween(500))
        },
    ) {
        SplashSignInScreen(
            onClick = onClick,
            googleButtonVisibility = googleButtonVisibility,
            context = context,
            loggedUser = loggedUser,
            navController = navController,
            viewModel = viewModel
        )
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.addHomeScreen(
    total: Double,
    categorySelected: MutableState<String>,
    coroutineScope: CoroutineScope,
    scrollState: LazyListState,
    products: List<Product>,
    viewModel: OrdersViewModel,
    loading: Boolean,
    loggedUser: MutableState<Client?>,
    scaffoldState: ScaffoldState,
    navController: NavController,
    orderList: List<Order>?,
    hasOpenOrder: MutableState<Boolean>
) {
    composable(
        route = Screen.HomeScreen.route,
        enterTransition = { _, _ ->
            slideInHorizontally(
                initialOffsetX = { -600 },
                animationSpec = tween(300, easing = FastOutLinearInEasing)
            )
        },
        exitTransition = { _, _ ->
            fadeOut(animationSpec = tween(500))
        },
        popEnterTransition = { _, _ ->
            fadeIn(animationSpec = tween(500))
        }
    ) {
        HomeScreen(
            total = total,
            categorySelected = categorySelected,
            coroutineScope = coroutineScope,
            scrollState = scrollState,
            products = products,
            viewModel = viewModel,
            loading = loading,
            loggedUser = loggedUser,
            scaffoldState = scaffoldState,
            navController = navController,
            orderList = orderList,
            hasOpenOrder = hasOpenOrder
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.addChatScreen() {
    composable(route = Screen.ChatScreen.route) {
        ChatScreen()
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.addOrderHistoryScreen(
    loggedUser: MutableState<Client?>,
    coroutineScope: CoroutineScope,
    navController: NavController,
    total: Double,
    orderList: List<Order>?,
    viewModel: OrdersViewModel,
    scaffoldState: ScaffoldState,
) {
    composable(route = Screen.OrderHistoryScreen.route) {
        OrderHistoryScreen(
            loggedUser = loggedUser,
            coroutineScope = coroutineScope,
            navController = navController,
            total = total,
            orderList = orderList,
            viewModel = viewModel,
            scaffoldState = scaffoldState
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
