package com.mdev.chatapp.data.remote.auth.model

data class TokensRefreshResponse(
    val message: String,
    val accessToken: String
)