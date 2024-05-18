package com.mdev.chatapp.data.remote.auth.model

data class TokenResponse(
    val account: Account,
    val apiKeyAIService: String,
    val message: String,
    val tokens: Tokens
)