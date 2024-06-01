package com.mdev.chatapp.data.remote.history

import android.util.Log
import com.mdev.chatapp.R
import com.mdev.chatapp.data.remote.api.ChatServerApi
import com.mdev.chatapp.data.remote.api.UpdateConversationTitle
import com.mdev.chatapp.data.remote.history.Model.GetConversationIdsRequest
import com.mdev.chatapp.data.remote.history.Model.GetConversationIdsResponse
import com.mdev.chatapp.domain.result.ApiResult
import com.mdev.chatapp.domain.repository.remote.HistoryRepository

class HistoryRepositoryImpl (
    private val chatServerApi: ChatServerApi
) : HistoryRepository {
    override suspend fun getConversations(userId: String): ApiResult<GetConversationIdsResponse> {
        return try {
            val response = chatServerApi.getConversationIds(GetConversationIdsRequest(userId))
            when (response.code()) {
                200 -> {
                    Log.d("HistoryViewModel", "getConversations: ${response.body()}")
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

    override suspend fun editConversationTitle(
        conversationId: String,
        title: String
    ): ApiResult<Unit> {
        return try {
            val response = chatServerApi.updateTitle(UpdateConversationTitle(conversationId, title))
            when (response.code()) {
                200 -> {
                    ApiResult.Success(Unit)
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
