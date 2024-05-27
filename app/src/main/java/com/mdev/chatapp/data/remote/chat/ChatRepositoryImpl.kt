package com.mdev.chatapp.data.remote.chat

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import com.mdev.chatapp.R
import com.mdev.chatapp.data.remote.chat.model.ConversationInitRequest
import com.mdev.chatapp.data.remote.chat.model.ConversationInitResponse
import com.mdev.chatapp.data.remote.chat.model.GetHistoryRequest
import com.mdev.chatapp.data.remote.chat.model.HistoryResponse
import com.mdev.chatapp.data.remote.chat.model.NewChatRequest
import com.mdev.chatapp.data.remote.chat.model.NewChatResponse
import com.mdev.chatapp.domain.repository.remote.ChatRepository
import com.mdev.chatapp.domain.result.ApiResult
import retrofit2.HttpException

class ChatRepositoryImpl(
    private val chatApi: ChatApi,
): ChatRepository {
    override suspend fun newChat(
        userId: String,
        conversationId: String,
        message: String
    ) : ApiResult<NewChatResponse> {
        return try{
            val response = chatApi.newChat(NewChatRequest(conversation_id = conversationId, id = userId, query = message))
            ApiResult.Success(response = response)
        } catch (e: HttpException){
            when (e.code()) {
                500 -> ApiResult.Error(R.string.server_error)
                else -> ApiResult.UnknownError("Api: " + e.code())
            }
        } catch (e: Exception){
            ApiResult.UnknownError("Other: " + e.message)
        }
    }

    override suspend fun loadHistoryChat(conversationId: String): ApiResult<HistoryResponse> {
        return try{
            val response = chatApi.getHistory(GetHistoryRequest(conversationId))
            ApiResult.Success(response = response)
        } catch (e: HttpException){
            when (e.code()) {
                404 -> ApiResult.Error(R.string.conversation_not_found)
                500 -> ApiResult.Error(R.string.server_error)
                else -> ApiResult.UnknownError(e.message())
            }
        }
    }

    override suspend fun initConversation(userId: String) : ApiResult<ConversationInitResponse> {
        return try{
            val response = chatApi.initChat(ConversationInitRequest(userId))
            Log.d("initCon", "initConversation: $response")
            ApiResult.Success(response = response)
        } catch (e: HttpException){
            when (e.code()) {
                404 -> ApiResult.Error(R.string.user_id_not_found)
                500 -> ApiResult.Error(R.string.server_error)
                else -> ApiResult.UnknownError(e.message())
            }
        }
    }


//    override suspend fun newLongChat(userId: String, conversationId: String, message: String): ApiResult<NewChatResponse> {
//        return try{
//            val call = chatApi.newLongChat(NewChatRequest(conversation_id = conversationId, id = userId, query = message))
//            val response = call.execute()
//            val stringBuilder = StringBuilder()
//            if (response.isSuccessful) {
//                val responseBody = response.body()
//                if (responseBody != null) {
//                    withContext(Dispatchers.IO) {
//                        responseBody.byteStream().bufferedReader().useLines { lines ->
//                            lines.forEach { line ->
//                                stringBuilder.append(line)
//                            }
//                        }
//                    }
//                    ApiResult.Success(response = NewChatResponse(messages = listOf(stringBuilder.toString())))
//                } else {
//                    ApiResult.Error(R.string.server_error)
//                }
//            } else {
//                ApiResult.Error(R.string.server_error)
//            }
//        } catch (e: HttpException){
//            when (e.code()) {
//                500 -> ApiResult.Error(R.string.server_error)
//                else -> ApiResult.UnknownError(e.message())
//            }
//        }
//    }
}