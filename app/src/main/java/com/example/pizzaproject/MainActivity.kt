package com.example.pizzaproject


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
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
import com.example.pizzaproject.ui.composables.DrawerContent
import com.example.pizzaproject.ui.composables.loadImageUri
import com.example.pizzaproject.ui.navigation.Screen
import com.example.pizzaproject.ui.screens.*
import com.example.pizzaproject.ui.theme.PizzaProjectTheme
import com.example.pizzaproject.ui.theme.Purple500
import com.example.pizzaproject.ui.theme.Purple700
import com.example.pizzaproject.utils.DEFAULT_IMAGE
import com.example.pizzaproject.utils.OrderStatus
import com.google.accompanist.navigation.animation.AnimatedComposeNavigator
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

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
            val stickyHeaderIndex1 = viewModel.stickyHeaderIndex1.value
            val stickyHeaderIndex2 = viewModel.stickyHeaderIndex2.value
            val loading = viewModel.loading.value
            val getTotal = viewModel.totalSum.value
            val bottomBarVisibility = viewModel.bottomBarVisibility
            val isCartOpen = viewModel.isCartOpen
            val cart = viewModel.cart.value
            val radioOptions = viewModel.radioOptions
            val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
            val fabVisibility = viewModel.floatingActionButtonVisibility
            val address = viewModel.addressTextField.value
            val googleButtonVisibility = viewModel.googleButtonVisibility
            val loggedUser = viewModel.loggedUser
            val topBarVisibility = viewModel.topBarVisibility
            val observationTextField = viewModel.observationTextField.value
            val hasOrderOpen = viewModel.hasOrderOpen

            PizzaProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    Scaffold(
                        scaffoldState = scaffoldState,
                        topBar = {
                            AnimatedVisibility(visible = topBarVisibility.value)
                            {
                                TopAppBar(
                                    title = {
                                        Text("Mario & Luigi")
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
                            }
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
                        drawerContent = {
                           DrawerContent(
                               loggedUser = loggedUser,
                               navController = navController,
                               scaffoldState = scaffoldState,
                               coroutineScope = coroutineScope
                           )
                        },
                        floatingActionButton = {
                            Crossfade(targetState = fabVisibility.value) {
                                if (it) {
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
                                                    totalPrice = getTotal,
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
                                                        hasOrderOpen.value = true
                                                    },
                                                    onFailure = {
                                                        Toast.makeText(
                                                            this,
                                                            "Erro ao enviar o pedido",
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    }
                                                )
                                            } else {
                                                Toast.makeText(
                                                    applicationContext,
                                                    "Insira o endereço para enviar o pedido",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        })
                                }
                            }
                        }
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
                                    navController = navController
                                )
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
                                    loggedUser = loggedUser,
                                    topBarVisibility = topBarVisibility
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
                                    observationTextField = observationTextField
                                )
                                addOrderHistoryScreen()
                                addChatScreen()
                            })
                    }
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
    radioOptions: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    viewModel: OrdersViewModel,
    total: Double,
    address: String,
    observationTextField: String,
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
            fabVisibility = fabVisibility,
            observationTextField = observationTextField
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

@ExperimentalMaterialApi
@ExperimentalAnimationApi
fun NavGraphBuilder.addSplashSignInScreen(
    googleButtonVisibility: MutableState<Boolean>,
    onClick: () -> Unit,
    context: Context,
    loggedUser: MutableState<Client?>,
    navController: NavController
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
            navController = navController
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
    fabVisibility: MutableState<Boolean>,
    topBarVisibility: MutableState<Boolean>,
    loggedUser: MutableState<Client?>
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
            loggedUser = loggedUser,
            topBarVisibility = topBarVisibility
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.addChatScreen(){
    composable(route = Screen.ChatScreen.route){
        ChatScreen()
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.addOrderHistoryScreen(){
    composable(route = Screen.OrderHistoryScreen.route){
        OrderHistoryScreen()
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
