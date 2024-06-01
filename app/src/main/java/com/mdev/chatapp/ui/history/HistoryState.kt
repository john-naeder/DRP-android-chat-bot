package com.mdev.chatapp.ui.history

import com.mdev.chatapp.data.remote.history.Model.Conversation

data class HistoryState (
    val isLoading: Boolean = false,
    val conversations: List<Conversation> = emptyList(),
    val newTitle: String = ""
)