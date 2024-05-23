package com.mdev.chatapp.data.remote.chat.model

data class Message(
    val role: String,
    val type: String,
    val content: String,
    val content_type: String,
    val extra_info: Any?
)