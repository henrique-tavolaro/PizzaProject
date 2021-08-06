package com.example.pizzaproject

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LocalPizza
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isFinite
import com.example.pizzaproject.ui.OrdersViewModel
import com.example.pizzaproject.ui.composables.CategoryCard
import com.example.pizzaproject.ui.composables.ImageCarousel
import com.example.pizzaproject.ui.theme.PizzaProjectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: OrdersViewModel by viewModels()

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val categorySelected = remember { mutableStateOf(Categories.PIZZAS) }

            PizzaProjectTheme {
                viewModel.getProductList()
                val products = viewModel.productsList.value
                val elevation = remember { mutableStateOf(2.dp)}
                val animateByState: State<Dp> = animateDpAsState(targetValue = elevation.value)

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
                            Text(
                                text = "Pizzaria Mario Luigi",
                                modifier = Modifier.padding(16.dp)
                            )

                            ImageCarousel(Images.image1, Images.image2, Images.image3)

                            Row(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            categorySelected.value = Categories.PIZZAS
                                        },
                                    elevation =  animateDpAsState(
                                        targetValue = if(categorySelected.value == Categories.PIZZAS) 8.dp else 2.dp).value
                                ){
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
                                        },
                                    elevation = animateDpAsState(
                                        targetValue = if(categorySelected.value == Categories.DESSERT) 8.dp else 2.dp).value
                                ){
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
                                        },
                                    elevation = animateDpAsState(
                                        targetValue = if(categorySelected.value == Categories.DRINKS) 8.dp else 2.dp).value
                                ){
                                    CategoryCard(
                                    icon = Icons.Filled.LocalDrink,
                                    category = Categories.DRINKS,
                                    contextDescription = Categories.DRINKS
                                    )
                                }

                            }
                            Divider(
                                thickness = 1.dp,
                                modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

object Images{
    const val image1 = R.drawable.pizza
    const val image2 = R.drawable.pizza2
    const val image3 = R.drawable.pizza3
}

object Categories {
    const val PIZZAS = "Pizzas"
    const val DESSERT = "Dessert"
    const val DRINKS = "Drinks"
}