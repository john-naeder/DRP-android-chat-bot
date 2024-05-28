package com.mdev.chatapp.ui.history

import com.mdev.chatapp.data.remote.history.Model.GetConversationIdsResponse
import com.mdev.chatapp.domain.result.ApiResult

interface HistoryRepository {
    suspend fun getConversations(userId: String): ApiResult<GetConversationIdsResponse>
}