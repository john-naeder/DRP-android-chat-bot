package com.mdev.chatapp.ui.history

import com.mdev.chatapp.data.remote.chat.model.GetConversationIdsResponse
import com.mdev.chatapp.domain.result.ApiResult
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    suspend fun getConversations(userId: String): ApiResult<GetConversationIdsResponse>
}