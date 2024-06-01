package com.mdev.chatapp.ui.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mdev.chatapp.R
import com.mdev.chatapp.data.remote.history.Model.Conversation
import com.mdev.chatapp.ui.common.BaseScreen
import com.mdev.chatapp.ui.common.Lottie
import com.mdev.chatapp.ui.common.nav_drawer.NavigateDrawerViewModel
import com.mdev.chatapp.ui.navgraph.Route
import com.mdev.chatapp.util.UIEvent
import dev.jeziellago.compose.markdowntext.MarkdownText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navDrawerViewModel: NavigateDrawerViewModel,
    historyViewModel: HistoryViewModel,
    onNavigateTo: (Route) -> Unit,
    onLogout: (Route) -> Unit,
    onBackClick: () -> Unit,
    onClick: (String) -> Unit
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val selectedItem = Route.HistoryScreen
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val state = historyViewModel.state

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
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        BaseScreen(
            scope = scope,
            drawerState = drawerState,
            selectedItem = selectedItem,
            navDrawerViewModel = navDrawerViewModel,
            scrollBehavior = scrollBehavior,
            content = {
                HistoryContent(
                    historyState = state,
                    onUiEvent = {
                        historyViewModel.onEvent(it)
                    },
                    onClick = onClick

                )
            }
        )
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun HistoryContent(
    historyState: HistoryState,
    onUiEvent: (HistoryUiEvent) -> Unit,
    onClick: (String) -> Unit
){
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            if (historyState.conversations.isNotEmpty()) {
                items(historyState.conversations.size) { index ->
                    val conversation = historyState.conversations[index]
                    HistoryItem(
                        conversation = conversation,
                        onUIEvent = onUiEvent,
                        onClick = onClick
                    )
                }
            } else item { Lottie(R.raw.empty_list) }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryItem(
    conversation: Conversation,
    onClick: (String) ->Unit,
    onUIEvent: (HistoryUiEvent) -> Unit,
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }

    val hapticFeedback = LocalHapticFeedback.current

    ElevatedCard(
        modifier = Modifier.combinedClickable(
            onClick = {
                onClick(conversation.conversation_id)
            },
            onLongClick = {
                isMenuExpanded = true
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        ),
        content = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                content = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.title),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        MarkdownText(
                            markdown = conversation.title,
                            maxLines = 1,
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.updated_at),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = conversation.updated_at,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    ItemDropDownMenu(
                        expanded = isMenuExpanded,
                        onDismiss = { isMenuExpanded = false },
                        onEdit = {
                            isMenuExpanded = false
                            isEditing = true
                        },
                    )
                }
            )
        }
    )
    if (isEditing) {
        EditDialog(
            title = conversation.title,
            onDismiss = { isEditing = false },
            onCancel = { isEditing = false },
            onConfirm = {
                isEditing = false
                onUIEvent(HistoryUiEvent.OnEditSaveClick(conversation))
            },
            onNewTileChange = {
                onUIEvent(HistoryUiEvent.OnTitleChanged(it))
            }
        )
    }
}

@Composable
fun ItemDropDownMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
) {
    val edit = stringResource(R.string.edit)
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        content = {
            DropdownMenuItem(
                onClick = onEdit,
                text = { Text(edit) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.TwoTone.Edit,
                        contentDescription = edit
                    )
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDialog(
    title: String,
    onDismiss: () -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    onNewTileChange: (String) -> Unit
) {

    var titleState by remember { mutableStateOf(title) }
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        content = {
            Surface {
                Column(
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    content = {
                        TextField(
                            value = titleState,
                            onValueChange = {
                                titleState = it
                                onNewTileChange(titleState)
                            },
                            placeholder = { Text(stringResource(R.string.history_title)) }
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(
                                16.dp,
                                Alignment.CenterHorizontally
                            )
                        ) {
                            Button(
                                content = { Text(stringResource(R.string.accept)) },
                                onClick = {
                                    onConfirm()
                                    onDismiss()
                                }
                            )
                            Button(
                                content = { Text(stringResource(R.string.cancel)) },
                                onClick = {
                                    onCancel()
                                    onDismiss()
                                }
                            )
                        }
                    }
                )
            }
        }
    )
}