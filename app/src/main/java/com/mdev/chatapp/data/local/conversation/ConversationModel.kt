package com.mdev.chatapp.data.local.conversation

import androidx.room.Entity

@Entity(primaryKeys = ["userId", "conversationId"])
data class ConversationModel(
    val userId: String,
    val conversationTitle: String,
    val conversationId: String
)
