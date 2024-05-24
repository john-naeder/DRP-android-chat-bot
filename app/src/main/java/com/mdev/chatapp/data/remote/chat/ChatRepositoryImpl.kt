package com.mdev.chatapp.data.remote.chat

import android.os.Build
import androidx.annotation.RequiresExtension
import com.mdev.chatapp.R
import com.mdev.chatapp.data.remote.chat.model.ConversationInitRequest
import com.mdev.chatapp.data.remote.chat.model.GetHistoryRequest
import com.mdev.chatapp.data.remote.chat.model.NewChatRequest
import com.mdev.chatapp.domain.repository.remote.ChatRepository
import com.mdev.chatapp.domain.result.ChatResult
import retrofit2.HttpException

class ChatRepositoryImpl(
    private val chatApi: ChatApi,
): ChatRepository{
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun newChat(userId: String, conversationId: String, message: String) : ChatResult<Unit> {
        return try{
            val response = chatApi.newChat(NewChatRequest(userId, conversationId, message))
            ChatResult.NewChatSuccess(response = response)
        } catch (e: HttpException){
            when (e.code()) {
                500 -> ChatResult.ChatError(R.string.server_error)
                else -> ChatResult.UnknownChatError(e.message())
            }
        }
    }

    override suspend fun getHistoryChat(conversationId: String) : ChatResult<Unit> {
        return try{
            val response = chatApi.getHistory(
                GetHistoryRequest(conversationId))
            ChatResult.GetHistoryChatSuccess(response = response)
        } catch (e: HttpException){
            when (e.code()) {
                404 -> ChatResult.ChatError(R.string.conversation_not_found)
                500 -> ChatResult.ChatError(R.string.server_error)
                else -> ChatResult.UnknownChatError(e.message())
            }
        }
    }

    override suspend fun initConversation(userId: String) : ChatResult<Unit> {
        return try{
            val response = chatApi.initChat(ConversationInitRequest(userId))
            ChatResult.InitConversationSuccess(response = response)
        } catch (e: HttpException){
            when (e.code()) {
                404 -> ChatResult.ChatError(R.string.user_id_not_found)
                500 -> ChatResult.ChatError(R.string.server_error)
                else -> ChatResult.UnknownChatError(e.message())
            }
        }
    }
}