package com.mdev.chatapp.ui.navgraph

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ChatBubble
import androidx.compose.material.icons.twotone.History
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.PeopleAlt
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Route(
    val route: String,
    val icon: ImageVector? = null,
) {
    data object AppStart : Route("app_start")
    data object OnBoarding : Route("onboarding")
    data object AuthScreen : Route("auth_screen")
    data object Login : Route("login_screen")
    data object Signup : Route("signup_screen")
    data object ForgotPassword : Route("forgot_password_screen")
    data object SignList : Route("sign_list_screen")
    data object UserSignedIn : Route("user_signed_in_screen")
    data object HomeNavigator : Route("home_navigator")
    data object HomeScreen : Route("home_screen", icon = Icons.TwoTone.Home)
    data object ChatScreen : Route("chat_screen", icon = Icons.TwoTone.ChatBubble)
    data object ProfileScreen : Route("profile_screen", icon = Icons.TwoTone.PeopleAlt)
    data object SettingsScreen : Route("settings_screen", icon = Icons.TwoTone.Settings)
    data object HistoryScreen : Route("history_screen", icon = Icons.TwoTone.History)
    data object About : Route("about", Icons.TwoTone.Info)


}
