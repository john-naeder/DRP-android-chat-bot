package com.mdev.chatapp.ui.navgraph

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ChatBubble
import androidx.compose.material.icons.twotone.History
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.Logout
import androidx.compose.material.icons.twotone.PeopleAlt
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.mdev.chatapp.R

sealed class Route(
    val route: String,
    val icon: ImageVector? = null,
    val title: Int? = null
) {
    data object AppStart : Route("app_start")
    data object OnBoarding : Route("onboarding")
    data object AuthNavigator : Route("auth_navigator", icon = Icons.TwoTone.Logout, title = R.string.logout)
    data object SignIn : Route("login_screen")
    data object Signup : Route("signup_screen")
    data object OTP : Route("otp_screen")
    data object ForgotPassword : Route("forgot_password_screen")
    data object AuthScreen : Route("AuthScreen")
    data object HomeNavigator : Route("home_navigator", icon = Icons.TwoTone.Home, title = R.string.home)
    data object HomeScreen : Route("home_screen", icon = Icons.TwoTone.Home, title = R.string.home)
    data object ProfileScreen : Route("profile_screen", icon = Icons.TwoTone.PeopleAlt, title = R.string.profile)
    data object SettingsScreen : Route("settings_screen", icon = Icons.TwoTone.Settings, title = R.string.settings)
    data object HistoryScreen : Route("history_screen", icon = Icons.TwoTone.History, title = R.string.history)
    data object AboutScreen : Route("about", icon = Icons.TwoTone.Info, title = R.string.about)
    data object ChatScreen : Route("chat_screen", icon = Icons.TwoTone.ChatBubble, title = R.string.chat)
}
