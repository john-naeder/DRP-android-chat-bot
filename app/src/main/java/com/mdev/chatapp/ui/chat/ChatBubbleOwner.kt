package com.mdev.chatapp.ui.chat

enum class ChatBubbleOwner {
    User, Assistant;
    companion object {
        fun of(owner: String): ChatBubbleOwner {
            return when(owner) {
                "user" -> User
                "assistant" -> Assistant
                else -> throw IllegalArgumentException("Invalid owner")
            }
        }
    }

}