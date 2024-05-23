package com.mdev.chatapp.data.remote.chat.model

data class ChatResponse(
    val messages: List<Message>,
    val conversation_id: String,
    val code: Int,
    val msg: String
)
