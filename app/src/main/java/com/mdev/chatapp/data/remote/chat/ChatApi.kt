package com.mdev.chatapp.data.remote.chat

import com.mdev.chatapp.data.remote.chat.model.ConversationInitRequest
import com.mdev.chatapp.data.remote.chat.model.ConversationInitResponse
import com.mdev.chatapp.data.remote.chat.model.GetHistoryRequest
import com.mdev.chatapp.data.remote.chat.model.HistoryResponse
import com.mdev.chatapp.data.remote.chat.model.NewChatRequest
import com.mdev.chatapp.data.remote.chat.model.NewChatResponse
import retrofit2.http.Body
import retrofit2.http.POST



interface ChatApi {
    @POST("conversation/init")
    suspend fun initChat(@Body request: ConversationInitRequest): ConversationInitResponse
    @POST("conversation/newChat")
    suspend fun newChat(@Body request: NewChatRequest): NewChatResponse
    @POST("conversation/history")
    suspend fun getHistory(@Body request: GetHistoryRequest): HistoryResponse
}