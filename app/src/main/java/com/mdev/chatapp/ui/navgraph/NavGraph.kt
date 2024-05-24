package com.mdev.chatapp.ui.navgraph

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.mdev.chatapp.ui.auth.AuthScreen
import com.mdev.chatapp.ui.auth.SignInScreen
import com.mdev.chatapp.ui.auth.SignUpScreen
import com.mdev.chatapp.ui.auth.viewmode.AuthViewModel
import com.mdev.chatapp.ui.auth.viewmode.SignInViewModel
import com.mdev.chatapp.ui.auth.viewmode.SignUpViewModel
import com.mdev.chatapp.ui.chat.ChatScreen
import com.mdev.chatapp.ui.chat.ChatViewModel
import com.mdev.chatapp.ui.home.HomeScreen
import com.mdev.chatapp.ui.nav_drawer.NavigateDrawerViewModel
import com.mdev.chatapp.ui.onboarding.OnBoardingScreen
import com.mdev.chatapp.ui.onboarding.OnBoardingViewModel

@Composable
fun NavGraph(
    startDestination: String
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    navBackStackEntry?.let {
        val currentDestination = it.destination.route
        Log.d("NavController", "Current destination: $currentDestination")

        val previousDestination = it.savedStateHandle.get<String>("previous_destination")
        Log.d("NavController", "Previous destination: $previousDestination")
    }

    NavHost(navController = navController, startDestination = startDestination) {
        navigation(route = Route.AppStart.route, startDestination = Route.OnBoarding.route){
            composable(Route.OnBoarding.route) {
                val viewModel: OnBoardingViewModel = hiltViewModel()
                OnBoardingScreen(onEvent = viewModel::onEvent)
            }
        }
        navigation(route = Route.AuthNavigator.route, startDestination = Route.AuthScreen.route) {
            composable(Route.AuthScreen.route){
                val viewModel: AuthViewModel = hiltViewModel()
                AuthScreen(
                    onAuthenticateSuccess = {
                        navController.navigate(it.route){
                            popUpTo(Route.AuthNavigator.route){
                                inclusive = true
                            }
                        }
                    },
                    onNavigateTo = {
                        navController.navigate(it.route)
                    },
                    viewModel = viewModel
                )
            }
            composable(Route.SignIn.route) {
                val viewModel: SignInViewModel = hiltViewModel()
                SignInScreen(
                    onSignSuccess = {
                        navController.navigate(Route.AuthScreen.route){
                            popUpTo(Route.AuthNavigator.route){
                                inclusive = true
                            }
                        }
                    },
                    onNavigateTo = {
                        navController.navigate(it.route){
                            popUpTo(Route.SignIn.route){
                                inclusive = true
                            }
                        }
                    },
                    viewModel = viewModel,
                    onBackClick = {
                        navController.navigate(Route.AuthNavigator.route){
                            popUpTo(Route.AuthNavigator.route){
                                inclusive = false
                            }
                        }
                    }
                )
            }
            composable(Route.Signup.route) {
                val viewModel: SignUpViewModel = hiltViewModel()
                SignUpScreen(
                    onSignUpSuccess = {
                        navController.navigate(Route.AuthScreen.route){
                            popUpTo(Route.AuthNavigator.route){
                                inclusive = true
                            }
                        }
                    },
                    onNavigateTo = {
                        navController.navigate(it.route){
                            popUpTo(Route.Signup.route){
                                inclusive = true
                            }
                        }
                    },
                    viewModel = viewModel,
                    onBackClick = {
                        navController.navigate(Route.AuthNavigator.route){
                            popUpTo(Route.AuthNavigator.route){
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable(Route.ForgotPassword.route) {
                // TODO
            }
            composable(Route.OTP.route) {
                // TODO
            }
        }

        navigation(route = Route.HomeNavigator.route, startDestination = Route.HomeScreen.route) {
            composable(Route.HomeScreen.route) {
                val viewModel: NavigateDrawerViewModel = hiltViewModel()
                HomeScreen(
                    onLogout = {
                        navController.navigate(Route.AuthNavigator.route){
                            popUpTo(Route.HomeNavigator.route){
                                inclusive = true
                            }
                        }
                    },
                    onNavigateTo = {
                        navController.navigate(it.route){
                            popUpTo(Route.AuthScreen.route){
                                inclusive = false
                            }
                        }
                    },
                    navDrawerViewModel = viewModel

                )
            }
            composable(Route.ProfileScreen.route) {
                // TODO
            }
            composable(Route.SettingsScreen.route) {
                // TODO
            }
            composable(Route.HistoryScreen.route) {
                // TODO
            }
            composable(Route.AboutScreen.route) {
                // TODO
            }
            composable(Route.ChatScreen.route) {
                val chatViewModel: ChatViewModel = hiltViewModel()
                val navigateDrawerViewModel: NavigateDrawerViewModel = hiltViewModel()

                ChatScreen(
                    onLogout = {
                        navController.navigate(Route.AuthNavigator.route){
                            popUpTo(Route.HomeNavigator.route){
                                inclusive = true
                            }
                        }
                    },
                    onNavigateTo = {
                        navController.navigate(it.route){
                            popUpTo(Route.AuthScreen.route){
                                inclusive = false
                            }
                        }
                    },
                    chatViewModel = chatViewModel,
                    navDrawerViewModel = navigateDrawerViewModel
                )
            }
        }
    }
}

//@Composable
//fun LogNavigation(destination: String) {
//    Log.d("NavigationLog", "Navigating to $destination")
//}
