package com.mdev.chatapp.ui.auth.viewmode

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.chatapp.R
import com.mdev.chatapp.domain.repository.remote.AuthRepository
import com.mdev.chatapp.domain.result.AuthResult
import com.mdev.chatapp.ui.auth.AuthState
import com.mdev.chatapp.ui.auth.AuthUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
): ViewModel(), AuthViewModelInterface {

    override var state by mutableStateOf(AuthState())
    private val uiEventChannel = Channel<AuthResult<Unit>>()
    val uiEvent = uiEventChannel.receiveAsFlow()

    override fun onEvent(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.UsernameChanged -> {
                val usernameError = event.value.isEmpty()
                val usernameErrorCode = if (usernameError) R.string.field_empty_error else R.string.null_field
                state = state.copy(
                    username = event.value,
                    usernameError = usernameError,
                    usernameErrorCode = usernameErrorCode
                )
            }

            is AuthUiEvent.PasswordChanged -> {
                val passwordError = event.value.isEmpty() || event.value.length < 6
                val passwordErrorCode = when {
                    event.value.isEmpty() -> R.string.field_empty_error
                    event.value.length < 6 -> R.string.password_length_error
                    else -> R.string.null_field
                }
                state = state.copy(
                    password = event.value,
                    passwordError = passwordError,
                    passwordErrorCode = passwordErrorCode
                )
            }

            is AuthUiEvent.RePasswordChanged -> {
                val rePasswordError =
                    event.value.isEmpty() || event.value.length < 6 || state.password != event.value
                val rePasswordErrorCode = when {
                    event.value.isEmpty() -> R.string.field_empty_error
                    event.value.length < 6 -> R.string.password_length_error
                    state.password != event.value -> R.string.password_not_match_error
                    else -> R.string.null_field
                }
                state = state.copy(
                    rePassword = event.value,
                    rePasswordError = rePasswordError,
                    rePasswordErrorCode = rePasswordErrorCode
                )
            }

            is AuthUiEvent.EmailChanged -> {
                val emailError = event.value.isEmpty()
                val emailErrorCode = if (emailError) R.string.field_empty_error else R.string.null_field
                state = state.copy(
                    email = event.value,
                    emailError = emailError,
                    emailErrorCode = emailErrorCode
                )
            }
            is AuthUiEvent.SignUp -> {
                signUp()
            }
            else -> {
                // do nothing
            }
        }
    }
    private fun signUp(){
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            val result = authRepository.signUp(
                state.username,
                state.password,
                state.email
            )
            uiEventChannel.send(result)
        }
        state = state.copy(isLoading = false)
    }
}