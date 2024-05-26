package com.mdev.chatapp.ui.chat

sealed class ChatUIEvent {
    data class OnInputMessageChanged(val value: String): ChatUIEvent()
    data class SendMessage(val message: String): ChatUIEvent()
    data object CancelSendMessage: ChatUIEvent()
}
