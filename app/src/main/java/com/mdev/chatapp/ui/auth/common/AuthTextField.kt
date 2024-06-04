package com.mdev.chatapp.ui.auth.common

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.mdev.chatapp.R
import com.mdev.chatapp.ui.auth.AuthState



@Composable
fun AuthTextField(
    modifier: Modifier = Modifier,
    @StringRes label: Int,
    state: AuthState,
    onPasswordChange: (String) -> Unit = {},
    onRePasswordChanged: (String) -> Unit = {},
    onUsernameChanged: (String) -> Unit = {},
    onEmailChanged: (String) -> Unit = {},
){
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }


    when (label) {
        R.string.password, R.string.repassword -> {
            PasswordTextField(
                modifier = modifier,
                label = label,
                password = password,
                onPasswordChange = { newPassword ->
                    password = newPassword
                    when (label) {
                        R.string.password -> {
                            onPasswordChange(newPassword)

                        }
                        R.string.repassword -> {
                            onRePasswordChanged(newPassword)
                        }
                    }
                },
                passwordVisible = passwordVisible,
                onPasswordVisibilityChange = { newPasswordVisible ->
                    passwordVisible = newPasswordVisible
                },
                supportingText = {
                    when(label){
                        R.string.password -> {
                            if (state.passwordError) {
                                Text(
                                    text = stringResource(id = state.passwordErrorCode),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                        R.string.repassword -> {
                            if (state.rePasswordError) {
                                Text(
                                    text = stringResource(id = state.rePasswordErrorCode),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                },
                isError = when (label) {
                    R.string.password -> state.passwordError
                    R.string.repassword -> state.rePasswordError
                    else -> false
                }
            )
        }

        R.string.username -> {
            TextField(
                modifier = modifier.background(MaterialTheme.colorScheme.background),
                label = {
                    Text(
                        text = stringResource(label),
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                value = state.username,
                onValueChange = { newUsername ->
                    onUsernameChanged(newUsername)
                },
                singleLine = true,
                placeholder = { Text("Abc123") },
//                colors = TextFieldDefaults.colors(
//                    focusedPlaceholderColor = if(MaterialTheme.colorScheme.) md_theme_dark_surfaceVariant else md_theme_light_tertiaryContainer,
//                    unfocusedPlaceholderColor = unfocusedTextFieldText,
//                    focusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
//                    unfocusedContainerColor = MaterialTheme.colorScheme.textFieldContainer
//                ),
                supportingText = {
                    if (state.usernameError)
                        Text(
                            text = stringResource(id = state.usernameErrorCode),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                },
                isError = state.usernameError
            )
        }
        R.string.email -> {
            TextField(
                modifier = modifier,
                label = {
                    Text(
                        text = stringResource(label),
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                value = state.email,
                onValueChange = { newEmail ->
                    onEmailChanged(newEmail)
                },
                singleLine = true,
                placeholder = { Text("duydeptrai@example.com") },
//                colors = TextFieldDefaults.colors(
//                    focusedPlaceholderColor = focusedTextFieldText,
//                    unfocusedPlaceholderColor = unfocusedTextFieldText,
//                    focusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
//                    unfocusedContainerColor = MaterialTheme.colorScheme.textFieldContainer
//                ),
                supportingText = {
                    if (state.emailError)
                        Text(
                            text = stringResource(id = state.emailErrorCode),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                },
                isError = state.emailError
            )
        }
    }
}


@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    label: Int,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    supportingText: @Composable () -> Unit,
    isError: Boolean = false
) {
    TextField(
        modifier = modifier,
        label = { Text(text = stringResource(label), style = MaterialTheme.typography.labelMedium) },
        value = password,
        onValueChange = onPasswordChange,
        singleLine = true,
        placeholder = { Text("HutaoVoTao@123") },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = { onPasswordVisibilityChange(!passwordVisible) }) {
                Icon(imageVector = image, contentDescription = description)
            }
        },
//        colors = TextFieldDefaults.colors(
//            focusedPlaceholderColor = focusedTextFieldText,
//            unfocusedPlaceholderColor = unfocusedTextFieldText,
//            focusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
//            unfocusedContainerColor = MaterialTheme.colorScheme.textFieldContainer
//        ),
        supportingText = {
            supportingText.invoke()
        },
        isError = isError
    )
}