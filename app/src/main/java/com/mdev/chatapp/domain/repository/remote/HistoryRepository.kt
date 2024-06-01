package com.mdev.chatapp.domain.repository.remote

import com.mdev.chatapp.data.remote.history.Model.GetConversationIdsResponse
import com.mdev.chatapp.domain.result.ApiResult

interface HistoryRepository {
    suspend fun getConversations(userId: String): ApiResult<GetConversationIdsResponse>
    suspend fun editConversationTitle(conversationId: String, title: String): ApiResult<Unit>
}