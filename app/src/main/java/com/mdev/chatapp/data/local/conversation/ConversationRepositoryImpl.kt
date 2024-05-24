package com.mdev.chatapp.data.local.conversation

import com.mdev.chatapp.domain.repository.local.ConversationRepository
import kotlinx.coroutines.flow.Flow

class ConversationRepositoryImpl(private val conversationDao: ConversationDao) : ConversationRepository {
    override suspend fun insertConversation(conversationModel: ConversationModel) {
        conversationDao.insertConversation(conversationModel)
    }

    override suspend fun deleteConversationById(conversationId: String) {
        conversationDao.deleteConversationById(conversationId)
    }

    override fun getAllConversationByUserId(userId: String): Flow<List<ConversationModel>> {
        return conversationDao.getAllConversationByUserId(userId)
    }


}