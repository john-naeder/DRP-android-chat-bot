package com.mdev.chatapp.data.remote.chat

import com.mdev.chatapp.data.remote.chat.model.ChatRequest
import com.mdev.chatapp.data.remote.chat.model.ChatResponse
import com.mdev.chatapp.util.Constants
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers(
        "Content-Type: application/json",
        "Accept: */*",
        "Authorization: Bearer " + Constants.CHAT_KEY_API
    )
    @POST("open_api/v2/chat")
    suspend fun sendChatRequest(@Body request: ChatRequest): ChatResponse
}