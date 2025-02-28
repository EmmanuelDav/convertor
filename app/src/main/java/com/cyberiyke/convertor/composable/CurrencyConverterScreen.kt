import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.cyberiyke.convertor.main.MainViewModel
import com.cyberiyke.convertor.ui.theme.ColorAccent
import com.cyberiyke.convertor.ui.theme.ColorPrimary
import com.cyberiyke.convertor.ui.theme.InputBg
import com.cyberiyke.convertor.utils.ConvertEvent
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
            .verticalScroll(rememberScrollState())
            .background(Color.White)
    ) {
        // Top Section: Icon and Sign Up Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
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
                    fontSize = 18.sp
                )
            }
        }

        // Title
        Text(
            text = "Currency Calculator",
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            color = ColorPrimary,
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            style = TextStyle(letterSpacing = 4.sp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .padding(top = 5.dp)
        ) {

            // Amount Input
            OutlinedTextField(
                value = amountValue,
                onValueChange = { amountValue = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Enter Value") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(15.dp))

            // Amount Input
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp)),
                color = InputBg // Background color for visibility
            ) {
                Text(
                    modifier = Modifier.padding(16.dp), // Adds padding inside the rounded area
                    text = when (val event = conversionState) {
                        is ConvertEvent.Empty -> "No conversion data"
                        is ConvertEvent.Loading -> "Loading..."
                        is ConvertEvent.Error -> "Error: ${event.errorMessage ?: "Unknown error"}"
                        is ConvertEvent.Success -> "Converted amount: ${event.result}"
                    },
                    color = Color.White // Text color
                )
            }


            Spacer(modifier = Modifier.height(15.dp))

            // From Currency Dropdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // From Currency Dropdown
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(InputBg, RoundedCornerShape(4.dp))
                        .clickable { expandFromCurrencyCode = true }
                        .padding(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.align(Alignment.CenterStart) // Aligns everything inside
                    ) {
                        AsyncImage(
                            model = mainViewModel.getFlagUrl(selectFromCurrencyCode),
                            contentDescription = "Flag of $selectFromCurrencyCode",
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp)) // Space between flag and text
                        Text(
                            text = selectFromCurrencyCode,
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    }

                    DropdownMenu(
                        expanded = expandFromCurrencyCode,
                        onDismissRequest = { expandFromCurrencyCode = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        mainViewModel.currencyToCountryMap.keys.forEach { currency ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        AsyncImage(
                                            model = mainViewModel.getFlagUrl(currency),
                                            contentDescription = "Flag of $currency",
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = currency, color = Color.Black)
                                    }
                                },
                                onClick = {
                                    selectFromCurrencyCode = currency
                                    expandFromCurrencyCode = false
                                }
                            )
                        }
                    }
                }


                Column {
                    Image(
                        painter = painterResource(id = R.drawable.ic_swap_horiz),
                        contentDescription = "Sort Icon",
                        modifier = Modifier.size(40.dp)
                    )
                }

                // To Currency Dropdown

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(InputBg, RoundedCornerShape(4.dp))
                        .clickable { expandToCurrencyCode = true }
                        .padding(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.align(Alignment.CenterStart) // Aligns everything inside
                    ) {
                        AsyncImage(
                            model = mainViewModel.getFlagUrl(selectToCurrencyCode),
                            contentDescription = "Flag of $selectToCurrencyCode",
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp)) // Space between flag and text
                        Text(
                            text = selectToCurrencyCode,
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    }

                    DropdownMenu(
                        expanded = expandToCurrencyCode,
                        onDismissRequest = { expandToCurrencyCode = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        mainViewModel.currencyToCountryMap.keys.forEach { currency ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        AsyncImage(
                                            model = mainViewModel.getFlagUrl(currency),
                                            contentDescription = "Flag of $currency",
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = currency, color = Color.Black)
                                    }
                                },
                                onClick = {
                                    selectToCurrencyCode = currency
                                    expandToCurrencyCode = false
                                }
                            )
                        }
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
                colors = ButtonDefaults.buttonColors(ColorAccent),
                shape = RoundedCornerShape(12.dp)

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
                text = "Mid Market Exchange Rate Unknow",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 16.sp,
                color = ColorPrimary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )

        }

        TabLayoutWithIndicator()
    }
}