package com.mdev.chatapp.ui.navgraph

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.mdev.chatapp.ui.home.HomeScreen
import com.mdev.chatapp.ui.auth.AuthScreen
import com.mdev.chatapp.ui.auth.SignInScreen
import com.mdev.chatapp.ui.auth.SignUpScreen
import com.mdev.chatapp.ui.auth.viewmode.AuthViewModel
import com.mdev.chatapp.ui.auth.viewmode.SignInViewModel
import com.mdev.chatapp.ui.auth.viewmode.SignUpViewModel
import com.mdev.chatapp.ui.home.HomeViewModel
import com.mdev.chatapp.ui.onboarding.OnBoardingScreen
import com.mdev.chatapp.ui.onboarding.OnBoardingViewModel

@Composable
fun NavGraph(
    startDestination: String
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        navigation(route = Route.AppStart.route, startDestination = Route.OnBoarding.route){
            composable(Route.OnBoarding.route) {
                val viewModel: OnBoardingViewModel = hiltViewModel()
                OnBoardingScreen(onEvent = viewModel::onEvent)
            }
        }
        navigation(route = Route.AuthScreen.route, startDestination = Route.Authenticate.route) {
            composable(Route.SignIn.route) {
                val viewModel: SignInViewModel = hiltViewModel()
                SignInScreen(
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable(Route.Signup.route) {
                val viewModel: SignUpViewModel = hiltViewModel()
                SignUpScreen(
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable(Route.Authenticate.route){
                val viewModel: AuthViewModel = hiltViewModel()
                AuthScreen(
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable(Route.ForgotPassword.route) {
                // TODO
            }
            composable(Route.SignList.route) {
                // TODO
            }
        }

        navigation(route = Route.HomeNavigator.route, startDestination = Route.HomeScreen.route) {
            composable(Route.HomeScreen.route) {
                val viewModel: HomeViewModel = hiltViewModel()
                HomeScreen(navController = navController, homeViewModel = viewModel)
            }
            composable(Route.ProfileScreen.route) {
                // TODO
            }
            composable(Route.SettingsScreen.route) {
                // TODO
            }
        }
    }
}

@Composable
fun LogNavigation(destination: String) {
    Log.d("NavigationLog", "Navigating to $destination")
}