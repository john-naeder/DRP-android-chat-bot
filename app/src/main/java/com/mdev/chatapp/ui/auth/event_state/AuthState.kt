package com.mdev.chatapp.ui.auth.event_state

data class AuthState(
    val isLoading: Boolean = false,
        val username: String = "",
        val password: String = "",
        val rePassword: String = "",
        val email: String = "",
        val signedInUsernameChanged: String = "",
)