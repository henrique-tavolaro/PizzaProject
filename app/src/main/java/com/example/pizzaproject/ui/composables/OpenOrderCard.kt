package com.example.pizzaproject.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pizzaproject.domain.models.Client
import com.example.pizzaproject.ui.navigation.Screen


@Composable
fun OpenOrderCard(
    navController: NavController
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                    navController.navigate(Screen.OrderHistoryScreen.route)
            },
        backgroundColor = Color.LightGray,
        shape = RoundedCornerShape(10.dp)
    ){
        Text(
            modifier = Modifier.padding(16.dp),
            text = "Você tem pedido(s) em aberto. Clique para ver!",
            fontStyle = FontStyle.Italic
        )
    }
}