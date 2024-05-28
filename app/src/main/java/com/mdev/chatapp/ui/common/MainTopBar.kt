package com.mdev.chatapp.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowForwardIos
import androidx.compose.material.icons.twotone.ArrowForwardIos
import androidx.compose.material.icons.twotone.History
import androidx.compose.material.icons.twotone.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mdev.chatapp.R
import com.mdev.chatapp.ui.navgraph.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    currentRoute: Route,
    onOpenDrawerClick: () -> Unit,
    onViewHistoryClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Surface(
        shadowElevation = 8.dp
    ) {
        TopAppBar(
            scrollBehavior = scrollBehavior,
            title = { Text(stringResource(currentRoute.title!!)) },
            navigationIcon = {
                IconButton(
                    onClick = onOpenDrawerClick,
                    content = {
                        Icon(
                            imageVector = Icons.TwoTone.Menu,
                            contentDescription = null
                        )
                    }
                )
            },
            actions = {
                if (currentRoute != Route.HistoryScreen) {
                    IconButton(
                        onClick = onViewHistoryClick,
                        content = {
                            Icon(
                                imageVector = Icons.TwoTone.History,
                                contentDescription = stringResource(R.string.history)
                            )
                        }
                    )
                }
                if (currentRoute != Route.HomeScreen) {
                    IconButton(
                        onClick = {
                            onBackClick()
                        },
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.TwoTone.ArrowForwardIos,
                                contentDescription = stringResource(R.string.settings)
                            )
                        }
                    )
                }
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerTopBar(
    scope: CoroutineScope,
    drawerState: DrawerState,
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name))},
        actions = {
            IconButton(
                onClick = {
                    scope.launch {
                        drawerState.close()
                    }
                },
                content = {
                    Icon(
                        imageVector = Icons.TwoTone.ArrowForwardIos,
                        contentDescription = stringResource(R.string.settings)
                    )
                }
            )
        }
    )
}