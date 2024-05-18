package com.mdev.chatapp.data.remote.auth.model

data class SignUpRequest(
    val username: String,
    val password: String,
    val email: String
)