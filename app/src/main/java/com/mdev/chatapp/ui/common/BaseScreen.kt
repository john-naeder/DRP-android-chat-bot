package com.mdev.chatapp.ui.common

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cabin
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.maxkeppeker.sheets.core.CoreDialog
import com.maxkeppeker.sheets.core.models.CoreSelection
import com.maxkeppeker.sheets.core.models.base.ButtonStyle
import com.maxkeppeker.sheets.core.models.base.Header
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.models.base.SelectionButton
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.mdev.chatapp.R
import com.mdev.chatapp.ui.common.nav_drawer.DrawerContent
import com.mdev.chatapp.ui.common.nav_drawer.NavDrawerUIEvent
import com.mdev.chatapp.ui.common.nav_drawer.NavigateDrawerViewModel
import com.mdev.chatapp.ui.navgraph.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
    val connectionState = rememberUseCaseState()
    var isConnectionState by remember {
        mutableStateOf(false)
    }

    val activity = (LocalContext.current as? Activity)

    navDrawerViewModel.connectionStateLiveData.observeForever {
        if (it) {
            isConnectionState = true
            connectionState.hide()
        } else {
            isConnectionState = false
            connectionState.show()
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
            Scaffold(
                topBar = {
                    MainTopAppBar(
                        scrollBehavior = scrollBehavior,
                        currentRoute = selectedItem,
                        onOpenDrawerClick = { scope.launch { drawerState.open() } },
                        onBackClick = {
                            navDrawerViewModel.onEvent(NavDrawerUIEvent.Back)
                        },
                        onViewHistoryClick = {
                            navDrawerViewModel.onEvent(NavDrawerUIEvent.History)
                        }
                    )
                },
                content = {
                    Box(
                        modifier = Modifier
                            .padding(it)
                            .fillMaxHeight(),
                        content = {
                            content()
                            CoreDialog(
                                state = connectionState,
                                selection = CoreSelection(
                                    withButtonView = true,
                                    negativeButton = SelectionButton(
                                        text = stringResource(id = R.string.exit),
                                        IconSource(Icons.Rounded.Notifications),
                                        ButtonStyle.TEXT
                                    ),
                                    positiveButton = SelectionButton(
                                        text = stringResource(id = R.string.retry),
                                        IconSource(Icons.Rounded.Cabin),
                                        ButtonStyle.ELEVATED
                                    ),
                                    onNegativeClick = {
                                        activity?.finish()
                                    },
                                    onPositiveClick = {
                                        connectionState.hide()
                                    }
                                ),
                                header = Header.Default(
                                    title = "Connection Lost",
                                    icon = IconSource(Icons.Rounded.Cabin),),
                                onPositiveValid = isConnectionState,
                                body = {
                                    Text(text = "Please check your internet connection and try again.")
                                },
                            )
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
