package com.mdev.chatapp.ui.navgraph

sealed class Route(
    val route: String
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
    data object SecretScreen : Route("secret_screen")
    data object HomeScreen : Route("home_screen")
    data object ChatScreen : Route("chat_screen")
    data object ProfileScreen : Route("profile_screen")
    data object SettingsScreen : Route("settings_screen")


}
