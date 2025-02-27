import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cyberiyke.converter.ui.theme.ColorPrimary
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabLayoutWithIndicator() {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Past 30 Days", "Past 90 Days")

    Column {
        // TabRow with Indicator
        Surface(
            modifier = Modifier
                .height(48.dp) // Set a fixed height
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)), // Rounded corners only at the top
            color = ColorPrimary // Background color
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.height(48.dp), // Match the height of the Surface
                containerColor = ColorPrimary, // Make the TabRow background transparent
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]), // Use tabIndicatorOffset
                        height = 6.dp,
                        color = Color(0xFF1ABC9C) // Indicator color
                    )
                },
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        modifier = Modifier.height(48.dp) // Match the height of the TabRow
                    )
                }
            }
        }

        // Content for each tab
        when (selectedTabIndex) {
            0 -> Text(
                text = "Content for Past 30 Days",
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center
            )
            1 -> Text(
                text = "Content for Past 90 Days",
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center
            )
        }
    }
}
