package com.mdev.chatapp.data.remote.auth.model

import com.mdev.chatapp.data.local.acccount.UserModel

data class TokenResponse(
    val account: UserModel,
    val apiKeyAIService: String,
    val message: String,
    val tokens: Tokens
)