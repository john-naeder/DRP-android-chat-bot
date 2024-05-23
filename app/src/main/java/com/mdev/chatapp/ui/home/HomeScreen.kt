package com.mdev.chatapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mdev.chatapp.R
import com.mdev.chatapp.ui.home.common.HomeTopAppBar
import com.mdev.chatapp.ui.home.common.Lottie
import com.mdev.chatapp.ui.navgraph.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel
) {
    val context = LocalContext.current
    val currentUser by homeViewModel.currentUser.collectAsState(initial = "")
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    var selectedItem by remember { mutableStateOf<Route>(Route.HomeScreen) }

    val listOfItem = listOf(
        Route.HomeNavigator,
        Route.HistoryScreen,
        Route.ProfileScreen,
        Route.AboutScreen,
        Route.SettingsScreen,
        Route.AuthScreen,
    )


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
        ModalNavigationDrawer(
            drawerContent = {
                DismissibleDrawerSheet {
                    Spacer(modifier = Modifier.height(16.dp))
                    Box {
                        Column {
                            listOfItem.forEach { item ->
                                NavigationDrawerItem(
                                    label = { Text(stringResource(id = item.title!!)) },
                                    selected = selectedItem == item,
                                    onClick = {
                                        selectedItem = item
                                        if (item == Route.AuthScreen) {
                                            homeViewModel.onEvent(HomeUiEvent.Logout)
                                        } else {
                                            navController.navigate(item.route)
                                        }
                                        scope.launch {
                                            drawerState.close()
                                        }
                                    },
                                    icon = {
                                        androidx.compose.material3.Icon(
                                            item.icon!!,
                                            contentDescription = stringResource(
                                                id = item.title!!
                                            )
                                        )
                                    },
                                    modifier = Modifier.widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.8f)
                                )
                            }
                        }

                    }
                }
            },
            drawerState = drawerState,
        ) {
            HomeContent(scrollBehavior, scope, drawerState, currentUser,
                onNewChat = {
                    // TODO
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    scrollBehavior: TopAppBarScrollBehavior,
    scope: CoroutineScope,
    drawerState: DrawerState,
    currentUser: String,
    onNewChat: () -> Unit,
) {
    Scaffold(
        topBar = {
            HomeTopAppBar(
                scrollBehavior = scrollBehavior,
                onNewChatClick = { onNewChat() },
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
                    Lottie(R.raw.robot)
                }
            }
        )
    }
}