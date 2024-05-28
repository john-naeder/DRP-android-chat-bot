package com.mdev.chatapp.ui.home

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.MarkChatUnread
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mdev.chatapp.R
import com.mdev.chatapp.ui.common.BaseScreen
import com.mdev.chatapp.ui.common.Lottie
import com.mdev.chatapp.ui.nav_drawer.NavDrawerUIEvent
import com.mdev.chatapp.ui.nav_drawer.NavigateDrawerViewModel
import com.mdev.chatapp.ui.navgraph.Route
import com.mdev.chatapp.util.UIEvent
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: (Route) -> Unit,
    onNavigateTo: (Route) -> Unit,
    onBackClick: () -> Unit,
    navDrawerViewModel: NavigateDrawerViewModel
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItem = Route.HomeScreen


    LaunchedEffect(navDrawerViewModel, context) {
        navDrawerViewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.Logout -> {
                    onLogout(Route.AuthNavigator)
                }
                is UIEvent.NavigateTo -> {
                    onNavigateTo(event.route)
                }
                is UIEvent.Back -> {
                    onBackClick()
                }
            }
        }
    }

    BaseScreen(
        scope = scope,
        drawerState = drawerState,
        selectedItem = selectedItem,
        navDrawerViewModel = navDrawerViewModel,
        scrollBehavior = scrollBehavior,
        content = {
            HomeContent(
                onNewChat = {
                    navDrawerViewModel.onEvent(NavDrawerUIEvent.NewChat)
                }
            )
        }
    )

}


@Composable
private fun HomeContent(
    onNewChat: () -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(6.dp),
        content = {
            HomeInnerContent(
                onNewChatClick = onNewChat
            )
        }
    )

}

@Composable
private fun HomeInnerContent(
    onNewChatClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .focusable()
            .wrapContentHeight()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ){
            Lottie(R.raw.robot)
        }
        FloatingActionButton(
            modifier = Modifier
                .padding(30.dp)
                .align(Alignment.End),
            onClick = { onNewChatClick() },
            content = {
                Icon(
                    imageVector = Icons.TwoTone.MarkChatUnread,
                    contentDescription = stringResource(R.string.chat)
                )
            },
        )
    }
}

