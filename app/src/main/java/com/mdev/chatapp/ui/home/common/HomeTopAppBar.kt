package com.mdev.chatapp.ui.home.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.EditNote
import androidx.compose.material.icons.twotone.ExitToApp
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.Logout
import androidx.compose.material.icons.twotone.Menu
import androidx.compose.material.icons.twotone.Settings
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onNavigationClick: () -> Unit,
    onNewChatClick: () -> Unit
) {
    val title = stringResource(R.string.app_name)
    Surface(
        shadowElevation = 8.dp
    ) {
        TopAppBar(
            scrollBehavior = scrollBehavior,
            title = { Text(title) },
            navigationIcon = {
                IconButton(
                    onClick = onNavigationClick,
                    content = {
                        Icon(
                            imageVector = Icons.TwoTone.Menu,
                            contentDescription = null
                        )
                    }
                )
            },
            actions = {
                IconButton(
                    onClick = onNewChatClick,
                    content = {
                        Icon(
                            imageVector = Icons.TwoTone.EditNote,
                            contentDescription = stringResource(R.string.new_chat)
                        )
                    }
                )
            }
        )
    }
}