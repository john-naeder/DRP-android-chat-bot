package com.mdev.chatapp.ui.history

import android.util.Log
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
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.collectAsState
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
import com.mdev.chatapp.data.local.conversation.ConversationModel
import com.mdev.chatapp.data.remote.chat.model.Conversation
import com.mdev.chatapp.ui.chat.ChatState
import com.mdev.chatapp.ui.common.BaseScreen
import com.mdev.chatapp.ui.common.Lottie
import com.mdev.chatapp.ui.nav_drawer.NavigateDrawerViewModel
import com.mdev.chatapp.ui.navgraph.Route
import com.mdev.chatapp.util.UIEvent
import dev.jeziellago.compose.markdowntext.MarkdownText
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navDrawerViewModel: NavigateDrawerViewModel,
    historyViewModel: HistoryViewModel,
    onItemClick: (String) -> Unit,
    onNavigateTo: (Route) -> Unit,
    onLogout: (Route) -> Unit
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val selectedItem = Route.HistoryScreen
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val hapticFeedback = LocalHapticFeedback.current

//    val conversations by historyViewModel.conversations.collectAsState(initial = emptyList())
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
                    conversations = state.conversations,
                    onItemClick = onItemClick,
                    hapticFeedback = hapticFeedback,
                    historyViewModel = historyViewModel
                )
            }
        )
        if (state.isLoading) {
            // This will be displayed on top of the AuthScreen content when isLoading is true
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f)), // This makes the screen look dimmed
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun HistoryContent(
//    conversations: List<ConversationModel>,
    conversations: List<Conversation>,
    onItemClick: (String) -> Unit,
    hapticFeedback: HapticFeedback,
    historyViewModel: HistoryViewModel
){
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            if (conversations.isNotEmpty()) {
                items(conversations.size) { index ->
                    val conversation = conversations[index]
                    HistoryItem(
//                        id = conversation.conversationId,
//                        title = conversation.conversationTitle,
                        id = conversation.conversation_id,
                        title = conversation.first_content,
                        onClick = {
//                            onItemClick(conversation.conversationId)
                            onItemClick(conversation.conversation_id)
                        },
                        onEdit = {
//                            historyViewModel.onEvent(HistoryUiEvent.OnEditSaveClick(conversation))
                        },
                        onDelete = {
//                            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
//                            historyViewModel.onEvent(HistoryUiEvent.OnDeleteHistory(conversation))
                        },
                        onNewTitleChanged = {
//                            historyViewModel.onEvent(HistoryUiEvent.OnTitleChanged(it))
                        }
                    )
                }
            } else item { Lottie(R.raw.empty_list) }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryItem(
    title: String,
    id: String,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onNewTitleChanged: (String) -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    var isDeleting by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }

    val hapticFeedback = LocalHapticFeedback.current

    ElevatedCard(
        modifier = Modifier.combinedClickable(
            onClick = onClick,
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
                            markdown = title,
                            maxLines = 1,
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.id),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = id,
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
                        onDelete = {
                            isDeleting = true
                            isMenuExpanded = false
                        }
                    )
                }
            )
        }
    )
    if (isDeleting) {
        DeleteConfirmModal(
            onDismiss = { isDeleting = false },
            onNegative = { isDeleting = false },
            onPositive = {
                isDeleting = false
                onDelete()
            }
        )
    }

    if (isEditing) {
        EditDialog(
            title = title,
            onDismiss = { isEditing = false },
            onCancel = { isEditing = false },
            onConfirm = {
                isEditing = false
                onEdit()
            },
            onNewTileChange = { onNewTitleChanged(it) }
        )
    }
}

@Composable
fun ItemDropDownMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val edit = stringResource(R.string.edit)
    val delete = stringResource(R.string.delete)
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
            DropdownMenuItem(
                onClick = onDelete,
                text = { Text(delete) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.TwoTone.Delete,
                        contentDescription = delete
                    )
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteConfirmModal(
    onDismiss: () -> Unit,
    onPositive: () -> Unit,
    onNegative: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        content = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .padding(bottom = 64.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                content = {
                    Text(stringResource(R.string.delete_confirm))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            16.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        Button(
                            content = { Text(stringResource(R.string.yes)) },
                            onClick = {
                                onPositive()
                                onDismiss()
                            }
                        )
                        Button(
                            content = { Text(stringResource(R.string.no)) },
                            onClick = {
                                onNegative()
                                onDismiss()
                            }
                        )
                    }
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
                            value = title,
                            onValueChange = {  onNewTileChange(it)},
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