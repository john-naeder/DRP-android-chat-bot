package com.mdev.chatapp.data.remote.chat.model

data class GetHistoryResponse(
    val messages: List<Message>
)