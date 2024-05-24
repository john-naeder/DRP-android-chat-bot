package com.mdev.chatapp.ui.auth.viewmode

import com.mdev.chatapp.ui.auth.AuthState
import com.mdev.chatapp.ui.auth.AuthUiEvent

interface AuthViewModelInterface {
    val state: AuthState
    fun onEvent(event: AuthUiEvent)
}