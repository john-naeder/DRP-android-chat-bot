package com.mdev.chatapp.data.remote.auth.model

import com.mdev.chatapp.data.local.user.AccountModel

data class TokenResponse(
    val account: AccountModel,
    val apiKeyAIService: String,
    val message: String,
    val tokens: Tokens
)