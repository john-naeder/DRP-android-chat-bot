package com.mdev.chatapp.ui.history

import com.mdev.chatapp.R
import com.mdev.chatapp.data.remote.chat.ChatApi
import com.mdev.chatapp.data.remote.chat.model.GetConversationIdsRequest
import com.mdev.chatapp.data.remote.chat.model.GetConversationIdsResponse
import com.mdev.chatapp.domain.result.ApiResult
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class HistoryRepositoryImpl (
    private val chatApi: ChatApi
) : HistoryRepository {
    override suspend fun getConversations(userId: String): ApiResult<GetConversationIdsResponse> {
        return try {
            val response = chatApi.getConversationIds(GetConversationIdsRequest(userId))
            ApiResult.Success(response)
        } catch (e: HttpException) {
            when (e.code()) {
                404 -> ApiResult.Error(R.string.user_id_not_found)
                500 -> ApiResult.Error(R.string.server_error)
                else -> ApiResult.UnknownError(e.message())
            }
        }
    }
}
