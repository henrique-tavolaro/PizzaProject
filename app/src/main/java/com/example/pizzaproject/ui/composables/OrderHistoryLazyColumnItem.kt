package com.example.pizzaproject.ui.composables

import androidx.compose.animation.*
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pizzaproject.domain.models.Order
import com.example.pizzaproject.utils.OrderStatus

@OptIn(ExperimentalAnimationApi::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun OrderHistoryLazyColumnItem(
    order: Order
) {
    val detailsVisibility = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth(),
        elevation = 4.dp,
        onClick = {
            detailsVisibility.value = !detailsVisibility.value
        },
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = "Id: ${order.id}",
                        fontSize = 14.sp,
                        color = if (order.status == OrderStatus.ACCEPTED || order.status == OrderStatus.OPEN) Color.Black
                        else if (order.status == OrderStatus.DELIVERED) Color.Gray
                        else Color.Red
                    )
                    Text(
                        text = "Data: ${order.date}",
                        fontSize = 14.sp,
                        color = if (order.status == OrderStatus.ACCEPTED || order.status == OrderStatus.OPEN) Color.Black
                        else if (order.status == OrderStatus.DELIVERED) Color.Gray
                        else Color.Red
                    )
                    Text(
                        text = "Status: ${order.status}",
                        color = if (order.status == OrderStatus.ACCEPTED || order.status == OrderStatus.OPEN) Color.Black
                        else if (order.status == OrderStatus.DELIVERED) Color.Gray
                        else Color.Red
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Total: R$ ${order.totalPrice}0",
                        color = if (order.status == OrderStatus.ACCEPTED || order.status == OrderStatus.OPEN) Color.Black
                        else if (order.status == OrderStatus.DELIVERED) Color.Gray
                        else Color.Red
                    )

                    Text(
                        text = "Ver detalhes do pedido",
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Italic,
                        color = if (order.status == OrderStatus.ACCEPTED || order.status == OrderStatus.OPEN) Color.Black
                        else if (order.status == OrderStatus.DELIVERED) Color.Gray
                        else Color.Red
                    )
                    IconButton(onClick = {
                    }
                    ) {
                        if (!detailsVisibility.value) {
                            Icon(
                                Icons.Filled.KeyboardArrowDown,
                                "see details icon"
                            )
                        } else {
                            Icon(
                                Icons.Filled.KeyboardArrowUp,
                                "see details icon"
                            )
                        }


                    }
                }
            }

            AnimatedVisibility(
                visible = detailsVisibility.value,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column() {
                    for (i in order.details) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(text = "${i.productCount}x   ${i.product}")
                            Text(
                                text = "R$ ${i.sumPrice}0",
                            )
                        }
                    }
                }


            }
        }


    }
}