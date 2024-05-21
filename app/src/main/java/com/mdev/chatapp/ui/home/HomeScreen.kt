package com.mdev.chatapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mdev.chatapp.R
import com.mdev.chatapp.ui.home.common.HomeTopAppBar
import com.mdev.chatapp.ui.home.common.Lottie
import com.mdev.chatapp.ui.navgraph.Route
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel
) {
    val context = LocalContext.current
    val currentUser by homeViewModel.currentUser.collectAsState(initial = "")
    LaunchedEffect(homeViewModel, context) {
        homeViewModel.result.collect { event ->
            when (event) {
                is HomeResult.Logout -> {
                    navController.navigate(Route.AuthScreen.route) {
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
            currentUser = currentUser,
            onLogout = {
                homeViewModel.onEvent(HomeUiEvent.Logout)
            },
            onNavigateTo = {
                navController.navigate(it.route){
                    popUpTo(Route.HomeScreen.route){
                        inclusive = true
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    onNavigateTo: (Route) -> Unit,
    onLogout: () -> Unit,
    currentUser: String
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    var selectedItem by remember { mutableStateOf<Route>(Route.HomeScreen) }

    val listOfItem = listOf(
        Route.HomeScreen,
        Route.HistoryScreen,
        Route.SettingsScreen,
        Route.AboutScreen,
        Route.ProfileScreen
    )

    DismissibleNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Box {
                    Column {
                        Spacer(modifier = Modifier.padding(16.dp))
                        listOfItem.forEach { item ->
                            Button(
                                onClick = {
                                    selectedItem = item
                                    onNavigateTo(item)
                                },
                                content = {
                                    Text(text = stringResource(id = item.title!!))
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                }
            }
        },
        drawerState = drawerState,
    ){
        Scaffold(
            topBar = {
                HomeTopAppBar(
                    scrollBehavior = scrollBehavior,
                    onSettingsClick = { onNavigateTo(Route.SettingsScreen) },
                    onLogoutClick = { onLogout() },
                    onNavigationClick = { scope.launch { drawerState.open() } },
                )
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp),
                content = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.welcome) + " $currentUser!",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Button(
                            content = { Text(stringResource(R.string.history)) },
                            onClick = { onNavigateTo(Route.HistoryScreen) },
                            modifier = Modifier.fillMaxWidth(.4f)
                        )
                        Lottie(R.raw.robot)
                    }
                }
            )
        }

    }
}