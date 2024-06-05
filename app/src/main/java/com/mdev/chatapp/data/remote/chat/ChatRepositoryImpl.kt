package com.mdev.chatapp.data.remote.chat

import android.util.Log
import com.mdev.chatapp.R
import com.mdev.chatapp.data.remote.api.ChatServerApi
import com.mdev.chatapp.data.remote.chat.model.ConversationInitRequest
import com.mdev.chatapp.data.remote.chat.model.ConversationInitResponse
import com.mdev.chatapp.data.remote.chat.model.GetHistoryRequest
import com.mdev.chatapp.data.remote.chat.model.HistoryResponse
import com.mdev.chatapp.data.remote.chat.model.NewChatRequest
import com.mdev.chatapp.data.remote.chat.model.NewChatResponse
import com.mdev.chatapp.domain.repository.remote.ChatRepository
import com.mdev.chatapp.domain.result.ApiResult

class ChatRepositoryImpl(
    private val chatServerApi: ChatServerApi,
): ChatRepository {
    override suspend fun newChat(
        userId: String,
        conversationId: String,
        message: String
    ) : ApiResult<NewChatResponse> {
        return try {
            val response = chatServerApi.newChat(
                NewChatRequest(
                    conversation_id = conversationId,
                    id = userId,
                    query = message
                )
            )
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


    override suspend fun loadHistoryChat(conversationId: String): ApiResult<HistoryResponse> {
        return try {
            val response = chatServerApi.getHistory(GetHistoryRequest(conversationId))
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

    override suspend fun initConversation(userId: String) : ApiResult<ConversationInitResponse> {
        return try {
            val response = chatServerApi.initChat(ConversationInitRequest(userId))
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