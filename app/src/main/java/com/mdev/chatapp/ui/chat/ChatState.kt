package com.mdev.chatapp.ui.chat


data class ChatState (
    val conversationTitle: String = "",
    val conversationId: String = "",
    val inputMessage: String = "",
    val currentUserId: String = "",
    val speechToTextError: Int = 0,

    val isWaitingForResponse: Boolean = false,
    val isInputVisibility: Boolean = true,
    val isLoading: Boolean = false,
    val isErrorOccurred: Boolean = false,
    val isListening: Boolean = false,

    var forceScroll: Int = 0,
)