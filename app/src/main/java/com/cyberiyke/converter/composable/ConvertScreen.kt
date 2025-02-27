package com.cyberiyke.converter.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cyberiyke.converter.main.MainViewModel
import com.cyberiyke.converter.utils.ConvertEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen(mainViewModel: MainViewModel) {
    var selectFromCurrencyCode by remember { mutableStateOf("from") }
    var selectToCurrencyCode by remember { mutableStateOf("to") }
    var amountValue by remember { mutableStateOf(TextFieldValue()) }

    var expandFromCurrencyCode by remember { mutableStateOf(false) }
    var expandToCurrencyCode by remember { mutableStateOf(false) }

    // Observe conversion state from the ViewModel
    val conversionState by mainViewModel.conversion.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 100.dp)
                .fillMaxWidth()
                .background(Color.Gray)
                .clickable {
                    expandFromCurrencyCode = true
                    //selectFromCurrencyCode = "from" // You may need to replace this with actual logic to select currency
                }
        ) {
            Text(
                text = selectFromCurrencyCode,
                color = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .padding(end = 16.dp)
            )
            DropdownMenu(
                expanded = expandFromCurrencyCode, // Adjust this condition based on your logic
                onDismissRequest = { expandFromCurrencyCode = false},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
                    .background(Color.White)
            ) {
                val from = listOf("USD", "UZS", "WON")
                from.forEach { currency ->
                    DropdownMenuItem(
                        text = {
                            Text(text = currency, color = Color.Black)
                        },
                        onClick = {
                            selectFromCurrencyCode = currency
                            expandFromCurrencyCode = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Similar logic for the "To" currency dropdown

        Box(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .background(Color.Gray)
                .clickable {
                    expandToCurrencyCode = true
                }
        ) {
            Text(
                text = selectToCurrencyCode,
                color = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .padding(end = 16.dp)
            )
            DropdownMenu(
                expanded = expandToCurrencyCode, // Adjust this condition based on your logic
                onDismissRequest = { expandToCurrencyCode = false},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
                    .background(Color.White)
            ) {
                val from = listOf("USD", "UZS", "WON")
                from.forEach { currency ->
                    DropdownMenuItem(
                        text = {
                            Text(text = currency, color = Color.Black)
                        },
                        onClick = {
                            selectToCurrencyCode = currency
                            expandToCurrencyCode = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = amountValue,
            onValueChange = {
                amountValue = it
            },
            label = { Text(text = "amount") }
        )

        Spacer(modifier = Modifier.height(60.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            onClick = {
                mainViewModel.getConvertRate(
                    from = selectFromCurrencyCode,
                    to = selectToCurrencyCode,
                    amount = amountValue.text
                )
            }
        ) {
            Text(text = "Convert Rate")
        }

        Spacer(modifier = Modifier.padding(30.dp))

        Text(
            text = when (val event = conversionState) {
                is ConvertEvent.Empty -> "No conversion data"
                is ConvertEvent.Loading -> "Loading..."
                is ConvertEvent.Error -> "Error: ${event.errorMessage ?: "Unknown error"}"
                is ConvertEvent.Success -> "Converted amount: ${getFormatted(event.result.result)}"
            }, // Show converted amount
            modifier = Modifier.fillMaxWidth(),
            fontSize = 30.sp,
            color = Color.Magenta,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
    }
}
private fun getFormatted(amount: Double): String {
    return String.format("%.2f", amount)
}