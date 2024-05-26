package com.mdev.chatapp.domain.repository.remote

import com.mdev.chatapp.data.remote.chat.model.ConversationInitResponse
import com.mdev.chatapp.data.remote.chat.model.HistoryResponse
import com.mdev.chatapp.data.remote.chat.model.NewChatResponse
import com.mdev.chatapp.domain.result.ApiResult

interface ChatRepository {
    suspend fun newChat(userId: String, conversationId: String, message: String): ApiResult<NewChatResponse>
    suspend fun loadHistoryChat(conversationId: String): ApiResult<HistoryResponse>
    suspend fun initConversation(userId: String): ApiResult<ConversationInitResponse>
}
