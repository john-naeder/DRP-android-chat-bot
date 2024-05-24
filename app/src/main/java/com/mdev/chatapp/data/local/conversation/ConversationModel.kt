package com.mdev.chatapp.data.local.conversation

import androidx.room.Entity

@Entity(primaryKeys = ["user_id", "conversation_id"])
data class ConversationModel(
    val user_id: String,
    val conversation_title: String,
    val conversation_id: String
)
