package com.mdev.chatapp.ui.main

import androidx.core.splashscreen.SplashScreen
import com.mdev.chatapp.ui.navgraph.Route

data class MainState(
    val isSplashScreen: Boolean = true,
    val startDestination: String = Route.AppStart.route,
    val isDarkTheme: Boolean = true,
    val language: String = "en"
)