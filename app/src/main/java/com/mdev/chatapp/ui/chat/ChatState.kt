package com.mdev.chatapp.ui.chat

data class ChatState (
    val conversationTitle: String = "",
    val conversationId: String = "",
    val inputMessage: String = "",
    val currentUserId: String = "",

    val isWaitingForResponse: Boolean = false,
    val isInputVisibility: Boolean = true,


    var forceScroll: Int = 0,
)