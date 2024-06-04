package com.mdev.chatapp.ui.main

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.mdev.chatapp.ui.theme.ChatAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import com.mdev.chatapp.ui.navgraph.NavGraph
import dagger.hilt.android.AndroidEntryPoint
import androidx.datastore.preferences.core.Preferences
import com.mdev.chatapp.util.Constants

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.USER_SETTINGS)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

//        val state = viewModel.state
        applyMaterialScheme(viewModel.state.isDarkTheme)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.isSplashScreen
            }
        }

        setContent {
            ChatAppTheme(darkTheme = viewModel.state.isDarkTheme){
                val systemController = rememberSystemUiController()

                SideEffect {
                    systemController.setStatusBarColor(
                        color = Color.Transparent,
                        darkIcons = !viewModel.state.isDarkTheme
                    )
                    systemController.setNavigationBarColor(
                        color = Color.Transparent,
                        darkIcons = !viewModel.state.isDarkTheme
                    )
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Box(
                        modifier = Modifier.background(MaterialTheme.colorScheme.background)
                    ) {
                        NavGraph(
                            startDestination = viewModel.state.startDestination,
                            onSwitchTheme = {
                                viewModel.onUIEvent(MainUIEvent.OnSwitchTheme)
                            },
                            onSwitchLanguage = {
                                viewModel.onUIEvent(MainUIEvent.OnSwitchLanguage(it))
                            },
                        )
                    }
                }
            }
        }
    }
    private fun applyMaterialScheme(isDarkTheme: Boolean) {
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}


