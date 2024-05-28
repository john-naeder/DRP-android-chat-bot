package com.mdev.chatapp.ui.chat

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Stop
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mdev.chatapp.R
import com.mdev.chatapp.data.remote.chat.model.HistoryResponse
import com.mdev.chatapp.ui.common.AnimatedDots
import com.mdev.chatapp.ui.common.BaseScreen
import com.mdev.chatapp.ui.common.TypewriterText
import com.mdev.chatapp.ui.nav_drawer.NavigateDrawerViewModel
import com.mdev.chatapp.ui.navgraph.Route
import com.mdev.chatapp.util.Constants
import com.mdev.chatapp.util.UIEvent
import dev.jeziellago.compose.markdowntext.MarkdownText


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    onLogout: (Route) -> Unit,
    onNavigateTo: (Route) -> Unit,
    onBackClick: () -> Unit,
    chatViewModel: ChatViewModel,
    navDrawerViewModel: NavigateDrawerViewModel
) {
    val chatState = chatViewModel.state.copy()

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
        scrollBehavior = scrollBehavior,
        navDrawerViewModel = navDrawerViewModel,
        content = {
            ChatContent(
                chatState = chatState,
                historyChat = chatViewModel.historyChats.value
            )
        },
        bottomBar = {
            Crossfade(
                targetState = chatViewModel.state.isInputVisibility,
                label = "",
                content = {
                    if (it) {
                        ChatInput(
                            modifier = Modifier.fillMaxWidth(),
                            viewModel = chatViewModel
                        )
                    } else {
                        BottomAppBar(
                            actions = { },
                            floatingActionButton = {
                                FloatingActionButton(
                                    onClick = { chatViewModel.onEvent(ChatUIEvent.CancelSendMessage) },
                                    content = {
                                        Icon(
                                            imageVector = Icons.TwoTone.Stop,
                                            contentDescription = stringResource(R.string.cancel)
                                        )
                                    }
                                )
                            }
                        )
                    }
                }
            )
        }

    )
}

@Composable
private fun ChatContent(
    chatState: ChatState,
    historyChat: HistoryResponse
) {
    val scrollState = rememberScrollState()

    if (scrollState.isScrollInProgress)
        LocalHapticFeedback.current.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    val chatSize by remember {
        mutableIntStateOf(historyChat.messages.size)
    }

    LaunchedEffect(chatSize) {
        val index = historyChat.messages.size - 1
        if (index > 0)
            scrollState.animateScrollTo(scrollState.maxValue)
    }

    LaunchedEffect(key1 = historyChat.messages.size) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        historyChat.messages.forEach {
            ChatBubble(
                content = it.content,
                owner = ChatBubbleOwner.of(it.role),
                isTypewriter = chatState.conversationId == Constants.INIT_CONVERSATION_ID && it.role == "assistant",
                typeWriterPass = { chatState.forceScroll = ++chatState.forceScroll }
            )
        }
        if (chatState.isWaitingForResponse) {
            ChatBubble(
                owner = ChatBubbleOwner.Assistant,
                content = {
                    Box(
                        content = { AnimatedDots() },
                        modifier = Modifier.padding(
                            vertical = 4.dp,
                            horizontal = 8.dp
                        )
                    )
                }
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatBubble(
    content: @Composable (ColumnScope.() -> Unit),
    owner: ChatBubbleOwner,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
) {
    val container = if (owner == ChatBubbleOwner.User) {
        MaterialTheme.colorScheme.secondaryContainer
    } else MaterialTheme.colorScheme.tertiaryContainer

    val shape = if (owner == ChatBubbleOwner.User) {
        RoundedCornerShape(
            topStart = 2.dp,
            topEnd = 16.dp,
            bottomEnd = 16.dp,
            bottomStart = 16.dp
        )
    } else {
        RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomEnd = 2.dp,
            bottomStart = 16.dp
        )
    }

    val arrangement = if (owner == ChatBubbleOwner.User) Arrangement.End else Arrangement.Start

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = arrangement
    ) {
        ElevatedCard(
            colors = CardDefaults.cardColors(containerColor = container),
            shape = shape,
            content = content,
            modifier = Modifier
                .padding(4.dp)
                .clip(shape)
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongClick
                )
        )
    }
}

@Composable
fun ChatBubble(
    content: String,
    owner: ChatBubbleOwner,
    isTypewriter: Boolean,
    typeWriterPass: () -> Unit = {}
) {
    var isExpanded by remember { mutableStateOf(true) }

    val haptic = LocalHapticFeedback.current
    val textCopied = stringResource(R.string.text_copied)
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    ChatBubble(
        owner = owner,
        onClick = { isExpanded = !isExpanded },
        onLongClick = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            clipboardManager.setText(AnnotatedString(content))
            Toast
                .makeText(context, textCopied, Toast.LENGTH_SHORT)
                .show()
        },
        content = {
            if (isTypewriter) {
                TypewriterText(
                    modifier = Modifier.padding(8.dp),
                    text = content.trim(),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 10,
                    typeWriterPass = typeWriterPass
                )
            } else {
//                Text(
//                    modifier = Modifier.padding(8.dp),
//                    text = content.trim(),
//                    overflow = TextOverflow.Ellipsis,
//                    maxLines = if (isExpanded) Int.MAX_VALUE else 10,
//                )
                MarkdownText(
                    markdown = content.trim(),
                    modifier = Modifier.padding(8.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 10,
//                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    )
}
