package com.mdev.chatapp.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mdev.chatapp.ui.auth.AuthViewModel
import com.mdev.chatapp.ui.navgraph.Route

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()

    ) {
    val context = LocalContext.current
    LaunchedEffect(homeViewModel, context) {
        homeViewModel.uiEvent.collect { event ->
            when (event) {
                is HomeResult.Logout -> {
                    navController.navigate(Route.AppStart.route) {
                        popUpTo(Route.HomeScreen.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
    Surface {
        HomeContent(
            onLogout = {
                homeViewModel.onEvent(HomeUiEvent.Logout)
            }
        )
    }
}

@Composable
private fun HomeContent(
    onLogout: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "You're authenticated!")
    }

    FloatingActionButton(onClick = { onLogout() }) {
        Text(text = "Logout")
    }
}