package com.mdev.chatapp.ui.main

import com.mdev.chatapp.ui.navgraph.Route
import com.mdev.chatapp.util.Constants

data class MainState(
    val isSplashScreen: Boolean = true,
    val startDestination: String = Route.AppStart.route,
    val isDarkTheme: Boolean = true,
    val language: String = Constants.DEFAULT_LANGUAGE
)