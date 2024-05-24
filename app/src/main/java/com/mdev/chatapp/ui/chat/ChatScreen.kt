package com.mdev.chatapp.ui.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.mdev.chatapp.ui.common.MainTopAppBar
import com.mdev.chatapp.ui.nav_drawer.DrawerContent
import com.mdev.chatapp.ui.nav_drawer.NavDrawerUIEvent
import com.mdev.chatapp.ui.nav_drawer.NavigateDrawerViewModel
import com.mdev.chatapp.ui.navgraph.Route
import com.mdev.chatapp.util.UIEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    onLogout: (Route) -> Unit,
    onNavigateTo: (Route) -> Unit,
    chatViewModel: ChatViewModel,
    navDrawerViewModel: NavigateDrawerViewModel
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val selectedItem = Route.ChatScreen
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(navDrawerViewModel, context) {
        navDrawerViewModel.uiEvent.collect {
            when (it) {
                is UIEvent.Logout -> {
                    onLogout(Route.AuthNavigator)
                }

                is UIEvent.NavigateTo -> {
                    onNavigateTo(it.route)
                }
            }
        }
    }

    Surface {
        ModalNavigationDrawer(
            drawerContent = {
                DrawerContent(scope, drawerState, selectedItem, onLogout = {
                    navDrawerViewModel.onEvent(NavDrawerUIEvent.Logout)
                }, onNavigateTo = {
                    navDrawerViewModel.onEvent(
                        when (it) {
                            Route.HomeScreen -> NavDrawerUIEvent.Home
                            Route.SettingsScreen -> NavDrawerUIEvent.Settings
                            Route.ChatScreen -> NavDrawerUIEvent.NewChat
                            Route.HistoryScreen -> NavDrawerUIEvent.History
                            Route.ProfileScreen -> NavDrawerUIEvent.Profile
                            Route.AboutScreen -> NavDrawerUIEvent.About
                            else -> NavDrawerUIEvent.Home
                        }
                    )
                })
            },
            drawerState = drawerState,
        ) {
            Scaffold(topBar = {
                MainTopAppBar(
                    scrollBehavior = scrollBehavior,
                    currentRoute = selectedItem,
                    onOpenDrawerClick = { scope.launch { drawerState.open() } },
                ) {}
            }) {
                Surface(
                    modifier = Modifier.padding(it)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ChatInput {
                            // TODO
                        }
                    }
                }
            }
        }
    }
}