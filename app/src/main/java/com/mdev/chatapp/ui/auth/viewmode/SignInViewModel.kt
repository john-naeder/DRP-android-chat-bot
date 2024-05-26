package com.mdev.chatapp.ui.auth.viewmode

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.chatapp.R
import com.mdev.chatapp.domain.repository.remote.AuthRepository
import com.mdev.chatapp.domain.result.ApiResult
import com.mdev.chatapp.ui.auth.AuthState
import com.mdev.chatapp.ui.auth.AuthUiEvent
import com.mdev.chatapp.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
): ViewModel(), AuthViewModelInterface {
    override var state by mutableStateOf(AuthState())
    private val uiEventChannel = Channel<ApiResult<Unit>>()
    val uiEvent = uiEventChannel.receiveAsFlow()

    override fun onEvent(event: AuthUiEvent) {
        when(event) {
            is AuthUiEvent.UsernameChanged -> {
                val usernameError = event.value.isEmpty()
                val usernameErrorCode = if (usernameError) R.string.field_empty_error else R.string.null_field
                state = state.copy(username = event.value, usernameError = usernameError, usernameErrorCode = usernameErrorCode)
            }
            is AuthUiEvent.PasswordChanged -> {
                val passwordError = event.value.isEmpty()
                val passwordErrorCode = if (passwordError) R.string.field_empty_error else R.string.null_field
                state = state.copy(password = event.value, passwordError = passwordError, passwordErrorCode = passwordErrorCode)
            }
            is AuthUiEvent.SignIn -> {
                viewModelScope.launch {
                    signIn()
                }
            }
            else -> {
                // do nothing
            }

        }
    }

    private fun signIn(){
        viewModelScope.launch {
        state = state.copy(isLoading = true)
            val result = authRepository.signIn(
                state.username,
                state.password
            )
            uiEventChannel.send(result)
        state = state.copy(isLoading = false)
        }
    }
}