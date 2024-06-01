package com.mdev.chatapp.ui.chat

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.twotone.Stop
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
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
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.CachePolicy
import com.mdev.chatapp.R
import com.mdev.chatapp.data.remote.chat.model.HistoryResponse
import com.mdev.chatapp.ui.common.AnimatedDots
import com.mdev.chatapp.ui.common.BaseScreen
import com.mdev.chatapp.ui.common.TypewriterText
import com.mdev.chatapp.ui.common.nav_drawer.NavigateDrawerViewModel
import com.mdev.chatapp.ui.navgraph.Route
import com.mdev.chatapp.util.UIEvent
import dev.jeziellago.compose.markdowntext.AutoSizeConfig
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
    val context = LocalContext.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val selectedItem = Route.ChatScreen
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
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

    val chatState = chatViewModel.state

    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }
    val recognizerIntent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN")
        }
    }



    val speechListener = remember {
        object : android.speech.RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                chatViewModel.onEvent(ChatUIEvent.IsListening(true))
            }

            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {
                chatViewModel.onEvent(ChatUIEvent.IsListening(false))
            }

            override fun onError(error: Int) {
                chatViewModel.onEvent(ChatUIEvent.IsListening(false))
            }

            override fun onResults(results: Bundle?) {
                chatViewModel.onEvent(ChatUIEvent.IsListening(false))
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                     chatViewModel.onEvent(ChatUIEvent.OnInputMessageChangedByListening(matches[0]))
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
    }

    LaunchedEffect(speechRecognizer) {
        speechRecognizer.setRecognitionListener(speechListener)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                speechRecognizer.startListening(recognizerIntent)
            }
        }
    )


    BaseScreen(
        scope = scope,
        drawerState = drawerState,
        selectedItem = selectedItem,
        scrollBehavior = scrollBehavior,
        navDrawerViewModel = navDrawerViewModel,
        content = {
            Box(
                modifier = Modifier.padding(
                    horizontal = 16.dp
                )
            ) {
                ChatContent(
                    chatState = chatState,
                    historyChat = chatViewModel.historyChats.value
                )
            }
        },
        bottomBar = {
            Crossfade(
                targetState = chatViewModel.state.isInputVisibility,
                label = "",
                content = {
                    if (it) {
                        ChatInput(
                            speechRecognizer,
                            recognizerIntent,
                            permissionLauncher,
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

    val shape = if (owner == ChatBubbleOwner.Assistant) {
        RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomEnd = 16.dp,
            bottomStart = 2.dp
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
) {
    var isExpanded by remember { mutableStateOf(true) }

    val haptic = LocalHapticFeedback.current
    val textCopied = stringResource(R.string.text_copied)
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val imageLoader = ImageLoader.Builder(context)
        .crossfade(true)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()

    ChatBubble(
        owner = owner,
        onClick = {
            Log.d("ChatBubble", "click detected")
            isExpanded = !isExpanded
        },
        onLongClick = {
            Log.d("ChatBubble", "Long click detected")
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            clipboardManager.setText(AnnotatedString(content))
            Toast
                .makeText(context, textCopied, Toast.LENGTH_SHORT)
                .show()
        },
        content = {
            MarkdownText(
                markdown = content.trim(),
                modifier = Modifier.padding(8.dp),
                maxLines = if (isExpanded) Int.MAX_VALUE else 10,
                linkColor = MaterialTheme.colorScheme.primary,
                truncateOnTextOverflow = true,
//                    isTextSelectable = true,
                imageLoader = imageLoader,
                autoSizeConfig = AutoSizeConfig(
                    autoSizeMinTextSize = 12,
                    autoSizeMaxTextSize = 16,
                    autoSizeStepGranularity = 2
                ),
                enableSoftBreakAddsNewLine = true,
            )
        }
    )
}





