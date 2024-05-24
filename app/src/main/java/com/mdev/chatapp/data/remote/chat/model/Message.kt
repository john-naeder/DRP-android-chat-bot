package com.mdev.chatapp.data.remote.chat.model

data class Message(
    val content: String,
    val content_type: String,
    val role: String? = "assistant",
    val type: String
)