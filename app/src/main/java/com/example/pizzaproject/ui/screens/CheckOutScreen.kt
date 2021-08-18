package com.example.pizzaproject.ui.screens

import android.opengl.Visibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pizzaproject.ui.OrdersViewModel
import com.example.pizzaproject.ui.theme.StickHeaderColor

@Composable
fun CheckOutScreen(
    radioOptions: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    viewModel: OrdersViewModel,
    total: Double,
    address: String,
    observationTextField: String,
    fabVisibility: MutableState<Boolean>
){
    fabVisibility.value = true

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        elevation = 4.dp
    ) {
        Column() {
            Text(
                text = "Forma de pagamento:",
                fontSize = 22.sp,
                fontStyle = FontStyle.Italic,
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
                    .fillMaxWidth()
            )
            SimpleRadioButtonComponent(
                radioOptions,
                selectedOption,
                onOptionSelected,
                viewModel
            )
            Text(
                text = "Endereço:",
                fontSize = 22.sp,
                fontStyle = FontStyle.Italic,
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
                    .fillMaxWidth()
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(top = 8.dp, start = 8.dp, bottom = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                value = address,
                onValueChange = viewModel::onAddressChange,
                label = {
                    Text("Endereço")
                }
            )
            Text(
                text = "Observações do pedido:",
                fontSize = 22.sp,
                fontStyle = FontStyle.Italic,
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
                    .fillMaxWidth()
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(top = 8.dp, start = 8.dp, bottom = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                value = observationTextField,
                onValueChange = viewModel::onObservationChange,
                label = {
                    Text("Observação")
                }
            )
            Text(
                text = "Total do pedido: R$ ${total}0",
                modifier = Modifier.padding(8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        }
    }
}

@Composable
fun SimpleRadioButtonComponent(
    radioOptions: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    viewModel: OrdersViewModel,
) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            radioOptions.forEach { text ->
                Row(
                    Modifier
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = {}
                        )
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = (text == selectedOption),
                        modifier = Modifier.padding(all = Dp(value = 8F)),
                        onClick = {
                            onOptionSelected(text)
//                            viewModel.habitStats(text, sdf, week, currDate)
                        }
                    )
                    Text(
                        text = text,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }
        }
}