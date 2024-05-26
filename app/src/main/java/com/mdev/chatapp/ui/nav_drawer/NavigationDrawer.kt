package com.mdev.chatapp.ui.nav_drawer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mdev.chatapp.ui.common.DrawerTopBar
import com.mdev.chatapp.ui.navgraph.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun DrawerContent(
    scope: CoroutineScope,
    drawerState: DrawerState,
    selectedItem: Route,
    onLogout: () -> Unit,
    onNavigateTo: (Route) -> Unit,
) {
    val listOfItem = listOf(
        Route.HomeScreen,
        Route.SettingsScreen,
        Route.ChatScreen,
        Route.HistoryScreen,
        Route.ProfileScreen,
        Route.AboutScreen,
        Route.AuthNavigator,
    )
    DismissibleDrawerSheet {
        Scaffold(
            modifier = Modifier.fillMaxWidth(0.8f),
            topBar = {
                DrawerTopBar(
                    scope = scope,
                    drawerState = drawerState,
                )
            },
        ) {
            Surface(modifier = Modifier
                .padding(it)
                .padding(12.dp))
            {
                Spacer(modifier = Modifier.height(16.dp))
                Box {
                    Column {
                        listOfItem.forEach { item ->
                            Spacer(modifier = Modifier.height(8.dp))
                            NavigationDrawerItem(
                                label = { Text(stringResource(id = item.title!!)) },
                                selected = selectedItem == item,
                                onClick = {
                                    when (item) {
                                        Route.AuthNavigator -> onLogout()
                                        selectedItem -> {
                                            // do nothing
                                        }
                                        else -> onNavigateTo(item)
                                    }
                                    scope.launch {
                                        drawerState.close()
                                    }
                                },
                                icon = {
                                    Icon(
                                        item.icon!!,
                                        contentDescription = stringResource(
                                            id = item.title!!
                                        )
                                    )
                                },
                                modifier = Modifier.widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.8f),
                            )
                        }
                    }
                }
            }
        }
    }
}