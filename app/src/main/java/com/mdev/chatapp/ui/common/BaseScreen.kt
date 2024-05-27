package com.mdev.chatapp.ui.common

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mdev.chatapp.ui.nav_drawer.DrawerContent
import com.mdev.chatapp.ui.nav_drawer.NavDrawerUIEvent
import com.mdev.chatapp.ui.nav_drawer.NavigateDrawerViewModel
import com.mdev.chatapp.ui.navgraph.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    scope: CoroutineScope,
    drawerState: DrawerState,
    selectedItem: Route,
    navDrawerViewModel: NavigateDrawerViewModel,
    scrollBehavior: TopAppBarScrollBehavior,
    content: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit? = {}
) {
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
            Scaffold(
                topBar = {
                    MainTopAppBar(
                        scrollBehavior = scrollBehavior,
                        currentRoute = selectedItem,
                        onOpenDrawerClick = { scope.launch { drawerState.open() } },
                    ) {}
                },
                content = {
                    Box(
                        modifier = Modifier
                            .padding(it)
                            .padding(start = 16.dp, end = 16.dp, top = 4.dp)
                            .fillMaxHeight(),
                        content = {
                            content()
                        }
                    )
                },
                bottomBar = {
                    bottomBar()
                }
            )
        }
    }
}
