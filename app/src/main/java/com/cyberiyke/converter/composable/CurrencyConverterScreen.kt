import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cyberiyke.converter.main.MainViewModel
import com.cyberiyke.converter.ui.theme.ColorAccent
import com.cyberiyke.converter.ui.theme.ColorPrimary
import com.cyberiyke.converter.utils.ConvertEvent
import com.cyberiyke.convertor.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyConverterView(mainViewModel: MainViewModel) {
    // State for currency selection
    var selectFromCurrencyCode by remember { mutableStateOf("EUR") }
    var selectToCurrencyCode by remember { mutableStateOf("EUR") }

    // State for amount input
    var amountValue by remember { mutableStateOf(TextFieldValue()) }

    // State for dropdown expansion
    var expandFromCurrencyCode by remember { mutableStateOf(false) }
    var expandToCurrencyCode by remember { mutableStateOf(false) }

    // Observe conversion state from the ViewModel
    val conversionState by mainViewModel.conversion.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(20.dp)
    ) {
        // Top Section: Icon and Sign Up Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { /* Handle icon click */ },
                modifier = Modifier.size(48.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_sort),
                    contentDescription = "Sort Icon"
                )
            }

            Button(
                onClick = { /* Handle sign up */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text(
                    text = "Sign Up",
                    color = ColorAccent,
                    fontSize = 16.sp
                )
            }
        }

        // Title
        Text(
            text = "Currency Calculator",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp),
            color = ColorPrimary,
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        // From Input
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
        ) {
            // From Currency Dropdown
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray, RoundedCornerShape(4.dp))
                    .clickable { expandFromCurrencyCode = true }
            ) {
                Text(
                    text = selectFromCurrencyCode,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterStart),
                    color = Color.White,
                    fontSize = 20.sp
                )
                DropdownMenu(
                    expanded = expandFromCurrencyCode,
                    onDismissRequest = { expandFromCurrencyCode = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    listOf("EUR", "USD", "GBP").forEach { currency ->
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

            Spacer(modifier = Modifier.height(15.dp))

            // Amount Input
            OutlinedTextField(
                value = amountValue,
                onValueChange = { amountValue = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Enter Value") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(15.dp))

            // To Currency Dropdown
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray, RoundedCornerShape(4.dp))
                    .clickable { expandToCurrencyCode = true }
            ) {
                Text(
                    text = selectToCurrencyCode,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterStart),
                    color = Color.White,
                    fontSize = 20.sp
                )
                DropdownMenu(
                    expanded = expandToCurrencyCode,
                    onDismissRequest = { expandToCurrencyCode = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    listOf("EUR", "USD", "GBP").forEach { currency ->
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

            Spacer(modifier = Modifier.height(30.dp))

            // Convert Button
            Button(
                onClick = {
                    mainViewModel.getConvertRate(
                        from = selectFromCurrencyCode,
                        to = selectToCurrencyCode,
                        amount = amountValue.text
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(ColorAccent)
            ) {
                Text(
                    text = "Convert",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Conversion Result
            Text(
                text = when (val event = conversionState) {
                    is ConvertEvent.Empty -> "No conversion data"
                    is ConvertEvent.Loading -> "Loading..."
                    is ConvertEvent.Error -> "Error: ${event.errorMessage ?: "Unknown error"}"
                    is ConvertEvent.Success -> "Converted amount: ${event.result}"
                },
                modifier = Modifier.fillMaxWidth(),
                fontSize = 20.sp,
                color = ColorPrimary,
                textAlign = TextAlign.Center
            )
        }
    }
}