package com.mdev.chatapp.ui.auth.viewmode

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.chatapp.data.local.user.UserSignedInModel
import com.mdev.chatapp.domain.repository.AuthRepository
import com.mdev.chatapp.domain.repository.UserSignedInRepository
import com.mdev.chatapp.ui.auth.AuthState
import com.mdev.chatapp.ui.auth.event.AuthResult
import com.mdev.chatapp.ui.auth.event.AuthUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userSignedInRepository: UserSignedInRepository
): ViewModel(), AuthViewModelInterface {

    override var state by mutableStateOf(AuthState())
    private val uiEventChannel = Channel<AuthResult<Unit>>()
    val uiEvent = uiEventChannel.receiveAsFlow()

    override fun onEvent(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.UsernameChanged -> {
                state = state.copy(username = event.value)
            }
            is AuthUiEvent.PasswordChanged -> {
                state = state.copy(password = event.value)
            }
            is AuthUiEvent.RePasswordChanged -> {
                state = state.copy(rePassword = event.value)
            }
            is AuthUiEvent.EmailChanged -> {
                state = state.copy(email = event.value)
            }
            is AuthUiEvent.SignUp -> {
                viewModelScope.launch {
                    if (state.username.isEmpty() || state.password.isEmpty() || state.rePassword.isEmpty() || state.email.isEmpty()) {
                        uiEventChannel.send(AuthResult.Error("Username, password, re-password or email is empty"))
                    } else if (state.password != state.rePassword) {
                        uiEventChannel.send(AuthResult.Error("Password and re-password do not match"))
                    } else {
                        signUp()
                    }
                }
            }
            else -> {
                // do nothing
            }
        }
    }
    private fun signUp(){
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = authRepository.signUp(
                state.username,
                state.password,
                state.email
            )

            userSignedInRepository.insertUser(UserSignedInModel(state.username))
            uiEventChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }
}