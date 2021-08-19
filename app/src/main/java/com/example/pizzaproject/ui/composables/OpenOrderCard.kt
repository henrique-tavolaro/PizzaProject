package com.example.pizzaproject.ui.composables

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
import com.example.pizzaproject.domain.models.Client


@Composable
fun OpenOrderCard(loggedUser: MutableState<Client?>){
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        backgroundColor = Color.LightGray,
        shape = RoundedCornerShape(10.dp)
    ){
        Text(
            text = "O restaurante está preparando o seu pedido!",
            fontStyle = FontStyle.Italic
        )
    }
}