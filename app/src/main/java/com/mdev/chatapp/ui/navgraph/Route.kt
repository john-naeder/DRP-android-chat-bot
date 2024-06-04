package com.mdev.chatapp.ui.navgraph

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.Logout
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.mdev.chatapp.R

sealed class Route(
    val route: String,
    val icon: ImageVector? = null,
    val title: Int? = null
) {
    data object AppStart : Route("app_start")
    data object OnBoarding : Route("onboarding")
    data object AuthNavigator : Route("auth_navigator", icon = Icons.AutoMirrored.TwoTone.Logout, title = R.string.logout)
    data object SignIn : Route("login_screen")
    data object Signup : Route("signup_screen")
    data object AuthScreen : Route("AuthScreen")
    data object HomeNavigator : Route("home_navigator", icon = Icons.Filled.Home, title = R.string.home)
    data object HomeScreen : Route("home_screen", icon = Icons.Filled.Home, title = R.string.home)
    data object ProfileScreen : Route("profile_screen", icon = Icons.Filled.PeopleAlt, title = R.string.profile)
    data object SettingsScreen : Route("settings_screen", icon = Icons.Filled.Settings, title = R.string.settings)
    data object HistoryScreen : Route("history_screen", icon = Icons.Filled.History, title = R.string.history)
    data object AboutScreen : Route("about", icon = Icons.Filled.Info, title = R.string.about)
    data object ChatScreen : Route("chat_screen", icon = Icons.Filled.ChatBubble, title = R.string.chat)
}
