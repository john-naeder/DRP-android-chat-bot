package com.mdev.chatapp.domain.result

import com.mdev.chatapp.data.remote.chat.model.ConversationInitResponse
import com.mdev.chatapp.data.remote.chat.model.GetHistoryResponse
import com.mdev.chatapp.data.remote.chat.model.NewChatResponse


sealed class ChatResult<T> (val data: T? = null) {
    class NewChatSuccess<T>(response: NewChatResponse): ChatResult<T>()
    class ChatError<T>(errMsgCode: Int): ChatResult<T>()
    class GetHistoryChatSuccess<T>(response: GetHistoryResponse): ChatResult<T>()
    class UnknownChatError<T>(message: String): ChatResult<T>()
    class InitConversationSuccess<T>(response: ConversationInitResponse): ChatResult<T>()
}