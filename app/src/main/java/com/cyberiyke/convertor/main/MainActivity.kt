package com.cyberiyke.convertor.main

import CurrencyConverterView
import TabLayoutWithIndicator
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.cyberiyke.convertor.ui.theme.ColorPrimaryDark
import com.cyberiyke.convertor.ui.theme.CurrentConverterjetpackComposeTheme
import com.cyberiyke.convertor.ui.theme.InputBg
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()
            val statusBarColor = InputBg

            // Set the status bar color
            LaunchedEffect(systemUiController) {
                systemUiController.setStatusBarColor(
                    color = statusBarColor,
                    darkIcons = false // Set to true for light status bar icons
                )
            }

            CurrentConverterjetpackComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CurrencyConverterView(mainViewModel)
                }
            }
        }
    }
}

