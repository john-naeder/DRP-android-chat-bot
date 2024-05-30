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
            is AuthUiEvent.RePasswordChanged -> {
                val rePasswordError = event.value != state.password
                val rePasswordErrorCode = if (rePasswordError) R.string.field_empty_error else R.string.null_field
                state = state.copy(
                    rePassword = event.value,
                    rePasswordError = rePasswordError,
                    rePasswordErrorCode = rePasswordErrorCode
                )
            }
            is AuthUiEvent.EmailChanged -> {
                val emailError = event.value.isEmpty()
                val emailErrorCode = if (emailError) R.string.field_empty_error else R.string.null_field
                state = state.copy(email = event.value, emailError = emailError, emailErrorCode = emailErrorCode)
            }
            is AuthUiEvent.OTPChanged -> {
                val otpError = event.value.isEmpty()
                val otpErrorCode = if (otpError) R.string.field_empty_error else R.string.null_field
                state = state.copy(otp = event.value, otpError = otpError, otpErrorCode = otpErrorCode)
            }
            is AuthUiEvent.SignIn -> {
                signIn()
            }
            is AuthUiEvent.SendResetPasswordOTP -> {
                sendOTP()
            }
            is AuthUiEvent.VerifyResetPasswordOTP -> {
                verifyOTP()
            }
            AuthUiEvent.ResetPassword -> {
                resetPassword()
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
    private fun sendOTP() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            authRepository.sendOTP(state.email)
            when (val result = authRepository.resetPasswordOTP(state.email)) {
                is ApiResult.Error -> {
                    uiEventChannel.send(result)
                    state = state.copy(isLoading = false)
                    return@launch
                }
                is ApiResult.UnknownError -> {
                    uiEventChannel.send(result)
                    state = state.copy(isLoading = false)
                    return@launch
                }
                is ApiResult.Success -> {
                    state = state.copy(isLoading = false, isInputEmailOTP = false, isVerifyOTP = true)
                }
                else -> {
                    // do nothing
                }
            }
        }
    }

    private fun verifyOTP() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val otpResult = authRepository.verifyOTP(
                email = state.email, otp = state.otp
            )
            when (otpResult){
                is ApiResult.Error -> {
                    uiEventChannel.send(otpResult)
                    state = state.copy(isLoading = false)
                    return@launch
                }
                is ApiResult.UnknownError -> {
                    uiEventChannel.send(otpResult)
                    state = state.copy(isLoading = false)
                    return@launch
                }
                is ApiResult.Success -> {
                    state = state.copy(isLoading = false, isVerifyOTP = false, isResetPassword = true, otp = "")
                }
                else -> {
                    // do nothing
                }
            }
        }
    }

    private fun resetPassword() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = authRepository.resetPassword(
                email = state.email,
                password = state.password
            )
            when (result) {
                is ApiResult.Error -> {
                    uiEventChannel.send(result)
                    state = state.copy(isLoading = false, isVerifyOTP = true)
                    return@launch
                }

                is ApiResult.UnknownError -> {
                    uiEventChannel.send(result)
                    state = state.copy(isLoading = false, isVerifyOTP = true)
                    return@launch
                }

                is ApiResult.Success -> {
                    resetState()
                    uiEventChannel.send(ApiResult.Error(R.string.reset_password_success))
                }
                is ApiResult.LogError -> {
                    // Do nothing
                }
            }
            state = state.copy(isLoading = false)
        }
    }

    private fun resetState() {
        state = AuthState()
    }
}