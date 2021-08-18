package com.example.pizzaproject.ui.screens

import CircularIndicator
import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LocalPizza
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pizzaproject.domain.models.Client
import com.example.pizzaproject.domain.models.Product
import com.example.pizzaproject.ui.OrdersViewModel
import com.example.pizzaproject.ui.ProductLazyColumnItem
import com.example.pizzaproject.ui.composables.CategoryCard
import com.example.pizzaproject.ui.composables.ImageCarousel
import com.example.pizzaproject.ui.theme.StickHeaderColor
import com.example.pizzaproject.utils.Categories
import com.example.pizzaproject.utils.Images
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
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
    loggedUser: MutableState<Client?>,

) {
    topBarVisibility.value = true
    bottomBarVisibility.value = getTotal != 0.0
    fabVisibility.value = false
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