package com.mdev.chatapp.ui.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.mdev.chatapp.R
import com.mdev.chatapp.ui.common.BaseScreen
import com.mdev.chatapp.ui.common.YoutubeScreen
import com.mdev.chatapp.ui.common.nav_drawer.NavigateDrawerViewModel
import com.mdev.chatapp.ui.navgraph.Route
import com.mdev.chatapp.util.Constants
import com.mdev.chatapp.util.UIEvent
import dev.jeziellago.compose.markdowntext.MarkdownText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navDrawerViewModel: NavigateDrawerViewModel,
    onBackClick: () -> Unit,
    onNavigateTo: (Route) -> Unit,
    onLogout: (Route) -> Unit
) {

    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val selectedItem = Route.AboutScreen
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
            LazyColumn {
                item { Spacer(modifier = Modifier.height(20.dp)) }
                item {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }
                item {
                    MarkdownText(
                        markdown = stringResource(id = R.string.about_text),
                    )
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.5f)
                    ) {
                        YoutubeScreen(
                            videoId = Constants.INTRO_VID_ID,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    )
}