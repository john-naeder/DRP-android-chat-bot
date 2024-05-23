package com.mdev.chatapp.data.remote.chat.model

data class ChatRequest(
    val conversation_id: String,
    val bot_id: String,
    val user: String,
    val query: String,
    val stream: Boolean = false
)