package com.example.pizzaproject.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.pizzaproject.R
import com.example.pizzaproject.domain.models.Client
import com.example.pizzaproject.ui.navigation.Screen
import com.example.pizzaproject.ui.theme.Purple500
import com.example.pizzaproject.ui.theme.Purple700
import com.example.pizzaproject.ui.theme.Red300
import com.example.pizzaproject.ui.theme.Red700
import com.example.pizzaproject.utils.DEFAULT_IMAGE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(
    loggedUser: MutableState<Client?>,
    navController: NavController,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState
){
    val items = listOf(
        Screen.HomeScreen,
        Screen.OrderHistoryScreen,
        Screen.ChatScreen
    )

    Column {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.32f)
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Red700,
                            Red300
                        )
                    )
                )
        ) {
            loggedUser.value?.photoUrl.let { url ->
                val image =
                    loadImageUri(
                        url = url?.toUri(),
                        defaultImage = DEFAULT_IMAGE
                    ).value
                image?.let { img ->
                    Image(
                        bitmap = img.asImageBitmap(),
                        modifier = Modifier
                            .height(152.dp)
                            .width(152.dp)
                            .padding(top = 16.dp, start = 16.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )
                }
            }
            loggedUser.value?.name?.let {
                Text(
                    modifier = Modifier
                        .padding(start = 24.dp, top = 8.dp),
                    text = it,

                    fontSize = 20.sp,
                    color = MaterialTheme.colors.onPrimary
                )
            }
            loggedUser.value?.email?.let {
                Text(
                    modifier = Modifier
                        .padding(start = 24.dp, bottom = 8.dp, top = 8.dp),
                    text = it,
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.onPrimary
                )
            }

        }
        Divider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.padding(8.dp))

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            DrawerItem(item = item, selected = currentRoute == item.route, onItemClick = {
                navController.navigate(item.route) {
                    launchSingleTop = true}
                coroutineScope.launch {
                    scaffoldState.drawerState.close()
                }
            })
        }
    }

}

@Composable
fun DrawerItem(item: Screen, selected: Boolean, onItemClick: (Screen) -> Unit) {
    val background = if (selected) Color.LightGray.copy(alpha = 0.4f) else Color.Transparent
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onItemClick(item) })
            .height(45.dp)
            .background(background)
            .padding(start = 16.dp)
    ) {
        Image(
            imageVector = item.icon!!,
            contentDescription = item.title,
//            colorFilter = ColorFilter.tint(Color.White),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(28.dp)
                .width(28.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = item.title!!,
            fontSize = 18.sp,
        )
    }
}