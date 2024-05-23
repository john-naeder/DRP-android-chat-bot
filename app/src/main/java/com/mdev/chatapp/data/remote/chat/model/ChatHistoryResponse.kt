package com.mdev.chatapp.data.remote.chat.model

data class ChatHistoryResponse(
    val bot_id: String,
    val chat_history: List<ChatHistory>,
    val conversation_id: String,
    val query: String,
    val stream: Boolean,
    val user: String
)