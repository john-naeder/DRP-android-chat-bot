package com.mdev.chatapp.ui.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mdev.chatapp.ui.auth.AuthState
import com.mdev.chatapp.util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {
    private var state by mutableStateOf(AuthState())
    private val uiEventChannel = Channel<UIEvent>()
    val uiEvent = uiEventChannel.receiveAsFlow()


}