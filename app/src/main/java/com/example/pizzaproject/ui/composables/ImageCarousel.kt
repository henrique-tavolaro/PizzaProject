package com.example.pizzaproject.ui.composables

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ImageCarousel(Image1: Int, Image2: Int, Image3: Int) {
    val infiniteTransition = rememberInfiniteTransition()
    val animatedImage: Int by infiniteTransition.animateValue(
        initialValue = 1,
        targetValue = 4,
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(9000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val drawable = remember { mutableStateOf<Int>(Image1) }
    Crossfade(targetState = animatedImage) {
        when (it % 3) {
            1 -> drawable.value = Image1
            2 -> drawable.value = Image2
            0 -> drawable.value = Image3

        }

        Image(
            painter = painterResource(id = drawable.value),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(150.dp),
            contentScale = ContentScale.FillWidth,
            contentDescription = "pizza images"
        )
    }

}