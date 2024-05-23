package com.mdev.chatapp.ui.navgraph

import androidx.compose.material.icons.Icons
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
    data object AuthScreen : Route("auth_screen", icon = Icons.TwoTone.Logout, title = R.string.logout)
    data object SignIn : Route("login_screen")
    data object Signup : Route("signup_screen")
    data object ForgotPassword : Route("forgot_password_screen")
    data object SignList : Route("sign_list_screen")
    data object Authenticate : Route("user_signed_in_screen")
    data object HomeNavigator : Route("home_navigator", icon = Icons.TwoTone.Home, title = R.string.home)
    data object HomeScreen : Route("home_screen")
    data object ProfileScreen : Route("profile_screen", icon = Icons.TwoTone.PeopleAlt, title = R.string.profile)
    data object SettingsScreen : Route("settings_screen", icon = Icons.TwoTone.Settings, title = R.string.settings)
    data object HistoryScreen : Route("history_screen", icon = Icons.TwoTone.History, title = R.string.history)
    data object AboutScreen : Route("about", Icons.TwoTone.Info, title = R.string.about)
}
