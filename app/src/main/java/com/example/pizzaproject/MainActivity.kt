package com.example.pizzaproject


import CircularIndicator
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LocalPizza
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pizzaproject.ui.OrdersViewModel
import com.example.pizzaproject.ui.ProductLazyColumnItem
import com.example.pizzaproject.ui.composables.CategoryCard
import com.example.pizzaproject.ui.composables.ImageCarousel
import com.example.pizzaproject.ui.theme.PizzaProjectTheme
import com.example.pizzaproject.ui.theme.StickHeaderColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: OrdersViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.Q)
    @OptIn(
        ExperimentalAnimationApi::class,
        androidx.compose.foundation.ExperimentalFoundationApi::class,
        androidx.compose.ui.ExperimentalComposeUiApi::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val categorySelected = viewModel.categorySelected
            val scrollState = rememberLazyListState()
            val coroutineScope = rememberCoroutineScope()
            val products = viewModel.productsList.value
            val stickyHeaderIndex1 = viewModel.stickyHeaderIndex1.value
            val stickyHeaderIndex2 = viewModel.stickyHeaderIndex2.value
            val loading = viewModel.loading.value
            PizzaProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text("Welcome") },
                                actions = {
                                    IconButton(
                                        onClick = {

                                        }) {
                                        Icon(
                                            imageVector = Icons.Filled.ShoppingCart,
                                            contentDescription = "shopping cart"
                                        )
                                    }
                                    Card() {
                                        Text("R$ 0.00")
                                    }
                                }
                            )
                        }
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
                                            text = "Pizzaria Mario Luigi",
                                            modifier = Modifier.padding(8.dp)
                                        )
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
                                            ProductLazyColumnItem(product = product)
                                        }


                                    }
                                }
                            }

                            LaunchedEffect(scrollState) {
                                snapshotFlow { scrollState.firstVisibleItemIndex }
//
                                    .distinctUntilChanged()
//
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
            }
        }
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