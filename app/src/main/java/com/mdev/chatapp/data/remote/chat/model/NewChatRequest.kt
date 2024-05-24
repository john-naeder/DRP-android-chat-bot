package com.mdev.chatapp.data.remote.chat.model

data class NewChatRequest(
    val conversation_id: String,
    val id: String,
    val query: String
)