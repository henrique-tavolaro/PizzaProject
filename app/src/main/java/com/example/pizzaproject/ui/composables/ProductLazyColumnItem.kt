package com.example.pizzaproject.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pizzaproject.R
import com.example.pizzaproject.domain.models.OrderInProgress
import com.example.pizzaproject.domain.models.Product
import com.example.pizzaproject.utils.OrdersEvent

@Composable
fun ProductLazyColumnItem(
    product: Product,
    viewModel: OrdersViewModel) {
       Row(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = product.name, fontSize = 16.sp)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
            ) {
            Text(
                text = "R$ ${product.price}0",
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(end = 8.dp)
            )
            IconButton(
                onClick = {
                    val orderInProgress = OrderInProgress(
                        product = product.name,
                        price = product.price
                    )
                    viewModel.onTriggerEvent(
                        OrdersEvent.AddProductToOrderEvent(orderInProgress))
                    viewModel.onTriggerEvent(OrdersEvent.GetTotalSumEvent)
                },
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_product_button)
                )
            }
        }

    }
}

val product1 = Product("id1", "Mussarela", 50.0, "pizza")

