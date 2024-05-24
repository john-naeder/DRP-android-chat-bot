package com.mdev.chatapp.domain.repository.local

import com.mdev.chatapp.data.local.conversation.ConversationModel
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    suspend fun insertConversation(conversationModel: ConversationModel)

    suspend fun deleteConversationById(conversationId: String)

    fun getAllConversationByUserId(userId: String): Flow<List<ConversationModel>>

}