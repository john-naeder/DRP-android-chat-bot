package com.mdev.chatapp.data.remote.chat.model

data class ChatHistory(
    val content: String,
    val content_type: String,
    val role: String,
    val type: String
)