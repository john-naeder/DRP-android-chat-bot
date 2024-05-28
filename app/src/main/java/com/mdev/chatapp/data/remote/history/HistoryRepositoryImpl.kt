package com.mdev.chatapp.data.remote.history

import com.mdev.chatapp.R
import com.mdev.chatapp.data.remote.chat.ChatApi
import com.mdev.chatapp.data.remote.history.Model.GetConversationIdsRequest
import com.mdev.chatapp.data.remote.history.Model.GetConversationIdsResponse
import com.mdev.chatapp.domain.result.ApiResult
import com.mdev.chatapp.ui.history.HistoryRepository

class HistoryRepositoryImpl (
    private val chatApi: ChatApi
) : HistoryRepository {
    override suspend fun getConversations(userId: String): ApiResult<GetConversationIdsResponse> {
        return try {
            val response = chatApi.getConversationIds(GetConversationIdsRequest(userId))
            when (response.code()) {
                200 -> {
                    val body = response.body()
                    if (body != null) {
                        ApiResult.Success(response = body)
                    } else {
                        ApiResult.Error(R.string.server_error)
                    }
                }
                404 -> ApiResult.Error(R.string.conversation_not_found)
                500 -> ApiResult.Error(R.string.server_error)
                else -> ApiResult.UnknownError(response.message())
            }
        } catch (e: Throwable) {
            ApiResult.UnknownError("Other error: " + e.message + " Type: " + e.javaClass.simpleName)
        }
    }
}
