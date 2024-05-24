package com.mdev.chatapp.domain.repository.remote

import com.mdev.chatapp.domain.result.ChatResult

interface ChatRepository {
    suspend fun newChat(userId: String, conversationId: String, message: String): ChatResult<Unit>
    suspend fun getHistoryChat(conversationId: String): ChatResult<Unit>
    suspend fun initConversation(userId: String): ChatResult<Unit>
}
