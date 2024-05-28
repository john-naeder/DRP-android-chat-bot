package com.mdev.chatapp.data.remote.chat

import com.mdev.chatapp.data.remote.chat.model.ConversationInitRequest
import com.mdev.chatapp.data.remote.chat.model.ConversationInitResponse
import com.mdev.chatapp.data.remote.history.Model.GetConversationIdsRequest
import com.mdev.chatapp.data.remote.history.Model.GetConversationIdsResponse
import com.mdev.chatapp.data.remote.chat.model.GetHistoryRequest
import com.mdev.chatapp.data.remote.chat.model.HistoryResponse
import com.mdev.chatapp.data.remote.chat.model.NewChatRequest
import com.mdev.chatapp.data.remote.chat.model.NewChatResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Streaming


interface ChatApi {
    @POST("conversation/init")
    suspend fun initChat(@Body request: ConversationInitRequest): Response<ConversationInitResponse>
    @POST("conversation/newChat")
    suspend fun newChat(@Body request: NewChatRequest): Response<NewChatResponse>
    @POST("conversation/getHistory")
    suspend fun getHistory(@Body request: GetHistoryRequest): Response<HistoryResponse>

    @POST("conversation/newChat")
    @Streaming
    fun newLongChat(@Body request: NewChatRequest): Response<Flow<NewChatResponse>>

    @POST("conversation/getConversations")
    suspend fun getConversationIds(@Body request: GetConversationIdsRequest): Response<GetConversationIdsResponse>
}