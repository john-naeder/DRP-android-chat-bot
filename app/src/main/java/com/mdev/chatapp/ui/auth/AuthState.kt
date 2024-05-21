package com.mdev.chatapp.ui.auth

data class AuthState(
    val isLoading: Boolean = false,
    val username: String = "",
    val password: String = "",
    val rePassword: String = "",
    val email: String = "",
    val signedInUsernameChanged: String = "",
)