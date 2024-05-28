package com.mdev.chatapp.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.core.view.WindowCompat
import androidx.datastore.preferences.preferencesDataStore
import com.mdev.chatapp.ui.theme.ChatAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mdev.chatapp.ui.auth.common.OTPTextField
import com.mdev.chatapp.ui.auth.common.OtpTextFieldDefaults
import com.mdev.chatapp.ui.navgraph.NavGraph
import dagger.hilt.android.AndroidEntryPoint


val Context.dataStore by preferencesDataStore("Auth")

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.splashCondition.value
            }
        }

        setContent {
            ChatAppTheme {
                val isSystemInDarkTheme = isSystemInDarkTheme()
                val systemController = rememberSystemUiController()

                SideEffect {
                    systemController.setStatusBarColor(
                        color = Color.Transparent,
                        darkIcons = !isSystemInDarkTheme
                    )
                    systemController.setNavigationBarColor(
                        color = Color.Transparent,
                        darkIcons = !isSystemInDarkTheme
                    )
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
                        NavGraph(startDestination = viewModel.startDestination.value)
                    }
                }
            }
        }
    }
}

