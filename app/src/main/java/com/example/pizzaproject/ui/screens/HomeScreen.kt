package com.example.pizzaproject.ui.screens

import CircularIndicator
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pizzaproject.domain.models.Client
import com.example.pizzaproject.domain.models.Product
import com.example.pizzaproject.ui.OrdersViewModel
import com.example.pizzaproject.ui.ProductLazyColumnItem
import com.example.pizzaproject.ui.composables.CategoryCard
import com.example.pizzaproject.ui.composables.DrawerContent
import com.example.pizzaproject.ui.composables.ImageCarousel
import com.example.pizzaproject.ui.composables.OpenOrderCard
import com.example.pizzaproject.ui.navigation.Screen
import com.example.pizzaproject.ui.theme.StickHeaderColor
import com.example.pizzaproject.utils.Categories
import com.example.pizzaproject.utils.Images
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalFoundationApi::class, androidx.compose.animation.ExperimentalAnimationApi::class,
    androidx.compose.material.ExperimentalMaterialApi::class
)
@Composable
fun HomeScreen(
    total: Double,
    categorySelected: MutableState<String>,
    coroutineScope: CoroutineScope,
    scrollState: LazyListState,
    products: List<Product>,
    viewModel: OrdersViewModel,
    loading: Boolean,
    loggedUser: MutableState<Client?>,
    scaffoldState: ScaffoldState,
    navController: NavController
) {
    val stickyHeaderIndex1 = viewModel.stickyHeaderIndex1.value
    val stickyHeaderIndex2 = viewModel.stickyHeaderIndex2.value
    val bottomBarVisibility = viewModel.bottomBarVisibility

    bottomBarVisibility.value = total != 0.0

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
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
    ) {
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            categorySelected.value = Categories.PIZZAS
                            coroutineScope.launch {
                                scrollState.animateScrollToItem(0)
                            }
                        },
                    elevation = animateDpAsState(
                        targetValue = if (categorySelected.value == Categories.PIZZAS) 8.dp else 2.dp
                    ).value
                ) {
                    CategoryCard(
                        icon = Icons.Filled.LocalPizza,
                        category = Categories.PIZZAS,
                        contextDescription = Categories.PIZZAS
                    )
                }
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            categorySelected.value = Categories.DESSERT
                            coroutineScope.launch {
                                scrollState.animateScrollToItem(stickyHeaderIndex1)
                            }
                        },
                    elevation = animateDpAsState(
                        targetValue = if (categorySelected.value == Categories.DESSERT) 8.dp else 2.dp
                    ).value
                ) {
                    CategoryCard(
                        icon = Icons.Filled.Cake,
                        category = Categories.DESSERT,
                        contextDescription = Categories.DESSERT
                    )
                }
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            categorySelected.value = Categories.DRINKS
                            coroutineScope.launch {
                                scrollState.animateScrollToItem(stickyHeaderIndex2)
                            }
                        },
                    elevation = animateDpAsState(
                        targetValue = if (categorySelected.value == Categories.DRINKS) 8.dp else 2.dp
                    ).value
                ) {
                    CategoryCard(
                        icon = Icons.Filled.LocalDrink,
                        category = Categories.DRINKS,
                        contextDescription = Categories.DRINKS
                    )
                }
            }
            Divider(
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
            )
            LazyColumn(
                state = scrollState
            ) {
                item(
                    content = {
                        Text(
                            text = "Bem-vindo ${loggedUser.value!!.name}",
                            modifier = Modifier.padding(8.dp)
                        )

//                        AnimatedVisibility(visible = orderList != null) {
//                            OpenOrderCard(loggedUser = loggedUser)
//                        }


                        ImageCarousel(Images.image1, Images.image2, Images.image3)
                    }
                )

                if (products.isNotEmpty()) {

                    val grouped = products
                        .groupBy { it.categoryOrder }

                    grouped.forEach { (initial, list) ->
                        stickyHeader {
                            Text(
                                text = if (initial == 1) Categories.PIZZAS
                                else if (initial == 2) Categories.DESSERT
                                else Categories.DRINKS,
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
                                    .fillMaxWidth(),
                                fontSize = 20.sp,
                                fontStyle = FontStyle.Italic
                            )
                            Divider(
                                thickness = 1.dp,
                                modifier = Modifier.padding(
                                    start = 8.dp,
                                    end = 250.dp
                                )
                            )
                        }

                        items(items = list.sortedBy { it.price }) { product ->
                            ProductLazyColumnItem(
                                product = product,
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }

            LaunchedEffect(scrollState) {
                snapshotFlow { scrollState.firstVisibleItemIndex }
                    .distinctUntilChanged()
                    .collect {
                        when {
                            it == 0 -> categorySelected.value = Categories.PIZZAS
                            it == stickyHeaderIndex1 + 1 -> categorySelected.value =
                                Categories.DESSERT
                            it == stickyHeaderIndex2 + 2 -> categorySelected.value =
                                Categories.DRINKS
                        }
                    }
            }
            CircularIndicator(loading = loading)
        }
    }
}
