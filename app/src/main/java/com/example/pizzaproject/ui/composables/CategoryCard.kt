package com.example.pizzaproject.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalPizza
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryCard(
    icon: ImageVector,
    category: String,
    contextDescription: String,
) {
    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Icon(imageVector = icon, contentDescription = contextDescription)
        Text(text = category)
    }
}


@Preview
@Composable
fun PreviewCategoryCard() {
    CategoryCard(
        icon = Icons.Filled.LocalPizza,
        category = "Pizzas",
        contextDescription = "",
    )
}
