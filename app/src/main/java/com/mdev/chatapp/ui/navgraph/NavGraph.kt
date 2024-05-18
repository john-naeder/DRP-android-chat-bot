package com.mdev.chatapp.ui.navgraph

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.mdev.chatapp.R
import com.mdev.chatapp.ui.home.SecretScreen
import com.mdev.chatapp.ui.auth.AuthScreen

@Composable
fun NavGraph(
    startDestination: String
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        navigation(route = Route.AppStart.route, startDestination = Route.OnBoarding.route){
            composable(Route.OnBoarding.route) {
                // TODO
            }
        }

        navigation(route = Route.AuthScreen.route, startDestination = Route.UserSignedIn.route) {
            composable(Route.Login.route) {
                AuthScreen(
                    R.string.login,
                    content = Route.Login.route,
                    navController = navController
                )
            }
            composable(Route.Signup.route) {
                AuthScreen(
                    R.string.signup,
                    content = Route.Signup.route,
                    navController = navController
                )
            }
            composable(Route.ForgotPassword.route) {
                // TODO
            }
            composable(Route.SignList.route) {
                // TODO
            }
            composable(Route.UserSignedIn.route){
                AuthScreen(
                    title = R.string.choose_profile,
                    content = Route.UserSignedIn.route,
                    navController = navController
                )
            }
        }

        navigation(route = Route.HomeNavigator.route, startDestination = Route.SecretScreen.route) {
            composable(Route.HomeScreen.route) {
                // TODO
            }

            composable(
                Route.ChatScreen.route,
                arguments = listOf(
                    navArgument("chatId") { type = NavType.StringType }
                )
            ) {
                // TODO
            }

            composable(Route.ProfileScreen.route) {
                // TODO
            }

            composable(Route.SettingsScreen.route) {
                // TODO
            }
            composable(Route.SecretScreen.route) {
                LogNavigation(destination = "SecretScreen")
                SecretScreen() // Test screen
            }
        }
    }
}

@Composable
fun LogNavigation(destination: String) {
    Log.d("NavigationLog", "Navigating to $destination")
}

//
//@Composable
//fun <T: ViewModel>NavBackStackEntry.sharedViewModel(navController: NavController): Any {
//    val navGraphRoute = destination.parent?.route?: return HiltViewModel()
//    val parentEntry =  remember(this){
//        navController.getBackStackEntry(navGraphRoute)
//    }
//    return viewModel(parentEntry)
//}