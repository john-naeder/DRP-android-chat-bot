package com.mdev.chatapp.ui.navgraph

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.mdev.chatapp.ui.about.AboutScreen
import com.mdev.chatapp.ui.auth.AuthScreen
import com.mdev.chatapp.ui.auth.SignInScreen
import com.mdev.chatapp.ui.auth.SignUpScreen
import com.mdev.chatapp.ui.auth.viewmode.AuthViewModel
import com.mdev.chatapp.ui.auth.viewmode.SignInViewModel
import com.mdev.chatapp.ui.auth.viewmode.SignUpViewModel
import com.mdev.chatapp.ui.chat.ChatScreen
import com.mdev.chatapp.ui.chat.ChatViewModel
import com.mdev.chatapp.ui.history.HistoryScreen
import com.mdev.chatapp.ui.history.HistoryViewModel
import com.mdev.chatapp.ui.home.HomeScreen
import com.mdev.chatapp.ui.common.nav_drawer.NavigateDrawerViewModel
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
    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        NavHost(navController = navController, startDestination = startDestination) {
            navigation(route = Route.AppStart.route, startDestination = Route.OnBoarding.route) {
                composable(Route.OnBoarding.route) {
                    val viewModel: OnBoardingViewModel = hiltViewModel()
                    OnBoardingScreen(onEvent = viewModel::onEvent)
                }
            }
            navigation(
                route = Route.AuthNavigator.route,
                startDestination = Route.AuthScreen.route
            ) {
                composable(Route.AuthScreen.route) {
                    val viewModel: AuthViewModel = hiltViewModel()
                    AuthScreen(
                        onAuthenticateSuccess = {
                            navController.navigate(it.route) {
                                popUpTo(Route.AuthNavigator.route) {
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
                            navController.navigate(it.route) {
                                popUpTo(Route.AuthNavigator.route) {
                                    inclusive = true
                                }
                            }
                        },
                        onNavigateTo = {
                            navController.navigate(it.route) {
                                popUpTo(Route.SignIn.route) {
                                    inclusive = true
                                }
                            }
                        },
                        viewModel = viewModel,
                        onBackClick = {
                            navController.navigate(Route.AuthNavigator.route) {
                                popUpTo(Route.AuthNavigator.route) {
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
                            navController.navigate(it.route) {
                                popUpTo(Route.AuthNavigator.route) {
                                    inclusive = true
                                }
                            }
                        },
                        onNavigateTo = {
                            navController.navigate(it.route) {
                                popUpTo(Route.Signup.route) {
                                    inclusive = true
                                }
                            }
                        },
                        viewModel = viewModel,
                        onBackClick = {
                            navController.navigate(Route.AuthNavigator.route) {
                                popUpTo(Route.AuthNavigator.route) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
            }

            navigation(
                route = Route.HomeNavigator.route,
                startDestination = Route.HomeScreen.route
            ) {
                composable(Route.HomeScreen.route) {
                    val viewModel: NavigateDrawerViewModel = hiltViewModel()
                    HomeScreen(
                        onLogout = {
                            navController.navigate(Route.AuthNavigator.route) {
                                popUpTo(Route.HomeNavigator.route) {
                                    inclusive = true
                                }
                            }
                        },
                        onNavigateTo = {
                            navController.navigate(it.route) {
//                                popUpTo(Route.HomeNavigator.route) {
//                                    inclusive = false
//                                }
                            }
                        },
                        onBackClick = {
                            navController.popBackStack()
                        },
                        navDrawerViewModel = viewModel

                    )
                }
                composable(Route.ProfileScreen.route) {
                    // TODO
                }
                composable(Route.HistoryScreen.route) {
                    val navDrawerViewModel: NavigateDrawerViewModel = hiltViewModel()
                    val historyViewModel: HistoryViewModel = hiltViewModel()
                    HistoryScreen(
                        navDrawerViewModel = navDrawerViewModel,
                        historyViewModel = historyViewModel,
                        onNavigateTo = {
                            navController.navigate(it.route) {
//                                popUpTo(Route.HomeNavigator.route) {
//                                    inclusive = false
//                                }
                            }
                        },
                        onLogout = {
                            navController.navigate(Route.AuthNavigator.route) {
                                popUpTo(Route.HomeNavigator.route) {
                                    inclusive = true
                                }
                            }
                        },
                        onItemClick = {
                            navController.navigate("${Route.ChatScreen.route}/$it"){
//                                popUpTo(Route.HistoryScreen.route) {
//                                    inclusive = false
//                                }
                            }
                        },
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }
                composable(Route.ChatScreen.route){
                    val chatViewModel: ChatViewModel = hiltViewModel()
                    val navigateDrawerViewModel: NavigateDrawerViewModel = hiltViewModel()

                    ChatScreen(
                        onLogout = {
                            navController.navigate(Route.AuthNavigator.route) {
                                popUpTo(Route.HomeNavigator.route) {
                                    inclusive = true
                                }
                            }
                        },
                        onNavigateTo = { route ->
                            navController.navigate(route.route) {
//                                popUpTo(Route.HomeNavigator.route) {
//                                    inclusive = false
//                                }
                            }
                        },
                        onBackClick = {
                            navController.popBackStack()
                        },
                        chatViewModel = chatViewModel,
                        navDrawerViewModel = navigateDrawerViewModel
                    )
                }
                composable(
                    route = "${Route.ChatScreen.route}/{conversationId}",
                    arguments = listOf(
                        navArgument("conversationId") {
                            type = NavType.StringType
                        }
                    )
                ) {
                    val chatViewModel: ChatViewModel = hiltViewModel()
                    val navigateDrawerViewModel: NavigateDrawerViewModel = hiltViewModel()

                    ChatScreen(
                        onLogout = {
                            navController.navigate(Route.AuthNavigator.route) {
                                popUpTo(Route.HomeNavigator.route) {
                                    inclusive = true
                                }
                            }
                        },
                        onNavigateTo = { route ->
                            navController.navigate(route.route) {
//                                popUpTo(Route.HomeNavigator.route) {
//                                    inclusive = false
//                                }
                            }
                        },
                        onBackClick = {
                            navController.popBackStack()
                        },
                        chatViewModel = chatViewModel,
                        navDrawerViewModel = navigateDrawerViewModel
                    )
                }
                composable(Route.AboutScreen.route) {
                    val navDrawerViewModel: NavigateDrawerViewModel = hiltViewModel()
                    AboutScreen(
                        navDrawerViewModel = navDrawerViewModel,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onNavigateTo = {
                            navController.navigate(it.route)
                        },
                        onLogout = {
                            navController.navigate(Route.AuthNavigator.route) {
                                popUpTo(Route.HomeNavigator.route) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

