package com.mdev.chatapp.ui.auth

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mdev.chatapp.R
import com.mdev.chatapp.data.local.user.UserModel
import com.mdev.chatapp.domain.result.ApiResult
import com.mdev.chatapp.ui.auth.common.AuthTextField
import com.mdev.chatapp.ui.auth.common.OTPTextField
import com.mdev.chatapp.ui.auth.common.OtpTextFieldDefaults
import com.mdev.chatapp.ui.auth.common.SocialMediaLogin
import com.mdev.chatapp.ui.auth.viewmode.AuthViewModel
import com.mdev.chatapp.ui.auth.viewmode.SignInViewModel
import com.mdev.chatapp.ui.auth.viewmode.SignUpViewModel
import com.mdev.chatapp.ui.navgraph.Route
import com.mdev.chatapp.ui.theme.BlueGray
import com.mdev.chatapp.ui.theme.Roboto
import com.mdev.chatapp.ui.theme.Shapes


@Composable
private fun TopSection(
    @StringRes header: Int,
    onBackClick: () -> Unit,
    isBackButtonVisible: Boolean = false,
) {
    Box(
        contentAlignment = Alignment.TopCenter,
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.35f),
            painter = painterResource(id = R.drawable.bookmark_shape),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 68.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            if (isBackButtonVisible) {
                IconButton(
                    onClick = { onBackClick() },
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.ArrowBackIosNew,
                        contentDescription = stringResource(id = R.string.back)
                    )
                }
            }
        }
        Row(
            modifier = Modifier.padding(top = 70.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(42.dp),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(id = R.string.app_logo),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = stringResource(id = R.string.auth_screen_header),
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    text = stringResource(id = R.string.auth_screen_title),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
        Text(
            modifier = Modifier
                .padding(bottom = 20.dp)
                .align(Alignment.BottomCenter),
            text = stringResource(id = header),
            style = MaterialTheme.typography.headlineLarge,
        )
    }
}


@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    onAuthenticateSuccess: (Route) -> Unit,
    onNavigateTo: (Route) -> Unit
) {
    val users = viewModel.users.collectAsState(initial = emptyList())

    val isLoading = viewModel.state.isLoading

    val context = LocalContext.current
    LaunchedEffect(viewModel, context) {
        viewModel.uiEvent.collect {
            when (it) {
                is ApiResult.Success -> {
                    onAuthenticateSuccess(Route.HomeNavigator)
                }

                is ApiResult.Error -> {
                    Toast.makeText(
                        context,
                        context.getString(it.message),
                        Toast.LENGTH_LONG
                    ).show()
                }

                is ApiResult.UnknownError -> {
                    Toast.makeText(
                        context,
                        it.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                TopSection(
                    header = R.string.auth_screen_header,
                    isBackButtonVisible = false,
                    onBackClick = {}
                )
                Spacer(modifier = Modifier.height(36.dp))
                UserSignedInContent(
                    users = users.value,
                    onChoose = { username ->
                        viewModel.onEvent(AuthUiEvent.SignedInUsernameChanged(username))
                        viewModel.onEvent(AuthUiEvent.SignedIn)
                    },
                    onDeleteUser = { username ->
                        viewModel.onEvent(AuthUiEvent.SignedInUsernameChanged(username))
                        viewModel.onEvent(AuthUiEvent.UnAuthenticatedUserChanged(username))
                    },
                    onSwitchToSignInClick = { onNavigateTo(Route.SignIn) },
                    onSwitchToSignUpClick = { onNavigateTo(Route.Signup) }
                )
            }
            if (isLoading) {
                // This will be displayed on top of the AuthScreen content when isLoading is true
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.8f)), // This makes the screen look dimmed
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun SignInScreen(
    viewModel: SignInViewModel,
    onSignSuccess: (Route) -> Unit,
    onNavigateTo: (Route) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    val isLoading = viewModel.state.isLoading

    remember { SnackbarHostState() }

    LaunchedEffect(viewModel, context) {
        viewModel.uiEvent.collect {
            when (it) {
                is ApiResult.Success -> {
                    onSignSuccess(Route.HomeNavigator)
                }

                is ApiResult.Error -> {
                    Toast.makeText(
                        context,
                        context.getString(it.message),
                        Toast.LENGTH_LONG
                    ).show()
                }

                is ApiResult.UnknownError -> {
                    Toast.makeText(
                        context,
                        it.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                TopSection(
                    header = R.string.login,
                    onBackClick = onBackClick,
                    isBackButtonVisible = true
                )
                Spacer(modifier = Modifier.height(20.dp))
                SignInContent(
                    switchToSignUpClick = { onNavigateTo(Route.Signup) },
                    onSignInClick = { viewModel.onEvent(AuthUiEvent.SignIn) },

                    onUsernameChanged = { viewModel.onEvent(AuthUiEvent.UsernameChanged(it)) },
                    onPasswordChange = { viewModel.onEvent(AuthUiEvent.PasswordChanged(it)) },
                    onEmailChanged = { viewModel.onEvent(AuthUiEvent.EmailChanged(it)) },
                    onOTPChanged = { viewModel.onEvent(AuthUiEvent.OTPChanged(it)) },
                    onRePasswordChanged = { viewModel.onEvent(AuthUiEvent.RePasswordChanged(it)) },

                    onForgotPasswordClick = {
                        viewModel.state = viewModel.state.copy(isInputEmailOTP = true)
                    },
                    onContinueToVerifyOTP = { viewModel.onEvent(AuthUiEvent.SendResetPasswordOTP) },
                    onOTPSubmit = { viewModel.onEvent(AuthUiEvent.VerifyResetPasswordOTP) },
                    onCompleteResetPassword = { viewModel.onEvent(AuthUiEvent.ResetPassword) },

                    onBackToInputEmail = {
                        viewModel.state =
                            viewModel.state.copy(isVerifyOTP = false, isInputEmailOTP = true)
                    },
                    onCancelClick = { viewModel.onEvent(AuthUiEvent.ResetState) },
                    onBackToSignInClick = {
                        viewModel.state = viewModel.state.copy(isInputEmailOTP = false)
                    },

                    state = viewModel.state
                )
            }
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.8f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}


@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    onSignUpSuccess: (Route) -> Unit,
    onNavigateTo: (Route) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val isLoading = viewModel.state.isLoading
    LaunchedEffect(viewModel, context) {
        viewModel.uiEvent.collect {
            when (it) {
                is ApiResult.Success -> {
                    onSignUpSuccess(Route.HomeNavigator)
                }

                is ApiResult.Error -> {
                    Toast.makeText(
                        context,
                        context.getString(it.message),
                        Toast.LENGTH_LONG
                    ).show()
                }

                is ApiResult.UnknownError -> {
                    Toast.makeText(
                        context,
                        it.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }
    }

    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                TopSection(
                    header = R.string.signup,
                    onBackClick = onBackClick,
                    isBackButtonVisible = true
                )
                Spacer(modifier = Modifier.height(20.dp))
                SignUpContent(
                    onSwitchToSignInClick = {
                        onNavigateTo(Route.SignIn)
                    },
                    onContinueToSignUp = {
                        viewModel.onEvent(AuthUiEvent.SendOTP)
                    },
                    state = viewModel.state,
                    onPasswordChange = { viewModel.onEvent(AuthUiEvent.PasswordChanged(it)) },
                    onRePasswordChanged = { viewModel.onEvent(AuthUiEvent.RePasswordChanged(it)) },
                    onUsernameChanged = { viewModel.onEvent(AuthUiEvent.UsernameChanged(it)) },
                    onEmailChanged = { viewModel.onEvent(AuthUiEvent.EmailChanged(it)) },
                    onOTPChanged = { viewModel.onEvent(AuthUiEvent.OTPChanged(it)) },
                    onOTPSubmit = { viewModel.onEvent(AuthUiEvent.SignUp) },
                    onOTPBackClick = { viewModel.state = viewModel.state.copy(isVerifyOTP = false) }
                )
            }
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.8f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun SignUpContent(
    onSwitchToSignInClick: () -> Unit,
    onContinueToSignUp: () -> Unit,
    onPasswordChange: (String) -> Unit,
    onRePasswordChanged: (String) -> Unit,
    onUsernameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onOTPChanged: (String) -> Unit,
    onOTPSubmit: () -> Unit,
    onOTPBackClick: () -> Unit,
    state: AuthState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
    ) {
        InputSignupSection(
            onSignUpClick = { onContinueToSignUp() },
            onUsernameChanged = { onUsernameChanged(it) },
            onEmailChanged = { onEmailChanged(it) },
            onPasswordChange = { onPasswordChange(it) },
            onRePasswordChanged = { onRePasswordChanged(it) },
            onSwitchToSignInClick = { onSwitchToSignInClick() },
            onOTPChanged = { onOTPChanged(it) },
            onOTPSubmit = { onOTPSubmit() },
            onOTPBackClick = { onOTPBackClick() },
            state = state
        )
    }
}

@Composable
private fun InputSignupSection(
    onSignUpClick: () -> Unit,
    onUsernameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRePasswordChanged: (String) -> Unit,
    onSwitchToSignInClick: () -> Unit,
    onOTPChanged: (String) -> Unit,
    onOTPSubmit: () -> Unit,
    onOTPBackClick: () -> Unit,
    state: AuthState,
) {

    AnimatedVisibility(
        visible = !state.isVerifyOTP,
        enter = fadeIn() + slideInHorizontally(),
        exit = fadeOut() + slideOutHorizontally()
    ) {

        Column {
            AuthTextField(
                label = R.string.username,
                state = state,
                modifier = Modifier.fillMaxWidth(),
                onUsernameChanged = onUsernameChanged
            )
            AuthTextField(
                label = R.string.email,
                onEmailChanged = onEmailChanged,
                state = state,
                modifier = Modifier.fillMaxWidth()
            )
            AuthTextField(
                label = R.string.password,
                state = state,
                onPasswordChange = onPasswordChange,
                modifier = Modifier.fillMaxWidth()
            )
            AuthTextField(
                label = R.string.repassword,
                state = state,
                onRePasswordChanged = onRePasswordChanged,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(5.dp))
            AuthButton(
                onClick = { onSignUpClick() },
                content = R.string.continue_to,
                enabled = state.passwordError.not()
                        && state.rePasswordError.not()
                        && state.password.isNotEmpty()
                        && state.rePassword.isNotEmpty()
                        && state.username.isNotEmpty()
                        && state.email.isNotEmpty()
                        && state.emailError.not()
            )
            Spacer(modifier = Modifier.height(10.dp))
            BottomSection(
                text = R.string.have_account,
                textBold = R.string.login_now,
                onClick = { onSwitchToSignInClick() }
            )
        }
    }
    AnimatedVisibility(
        visible = state.isVerifyOTP,
        enter = fadeIn() + slideInHorizontally(),
        exit = fadeOut() + slideOutHorizontally()
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.45f)
            ) {
                OTPTextField(
                    value = state.otp, // Initial value
                    onTextChanged = { onOTPChanged(it) },
                    numDigits = 6,
                    isMasked = true,
                    digitContainerStyle = OtpTextFieldDefaults.outlinedContainer(),
                    textStyle = MaterialTheme.typography.titleLarge,
                    isError = state.otpError
                )
            }
            AuthButton(
                onClick = { onOTPSubmit() },
                content = R.string.continue_to,
                enabled = state.otpError.not()
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = { onOTPBackClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.back))
            }
        }
    }
}



@Composable
fun SignInContent(
    switchToSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,

    onUsernameChanged: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRePasswordChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onOTPChanged: (String) -> Unit,

    onContinueToVerifyOTP: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onCompleteResetPassword: () -> Unit,
    onOTPSubmit: () -> Unit,

    onBackToInputEmail: () -> Unit,
    onBackToSignInClick: () -> Unit,
    onCancelClick: () -> Unit,
    state: AuthState
) {

    AnimatedVisibility(
        visible = !(state.isResetPassword || state.isVerifyOTP || state.isInputEmailOTP),
        enter = fadeIn() + slideInHorizontally(),
        exit = fadeOut() + slideOutHorizontally()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ) {
            InputLoginSection(
                loginClick = onSignInClick,
                onUsernameChanged = onUsernameChanged,
                onPasswordChange = onPasswordChange,
                onForgotPasswordClick = onForgotPasswordClick,
                state = state
            )
            Spacer(modifier = Modifier.height(20.dp))
            SocialMediaMethodSection()
            Spacer(modifier = Modifier.height(20.dp))
            BottomSection(
                text = R.string.dont_have_account,
                textBold = R.string.signup_now,
                onClick = { switchToSignUpClick() }
            )
        }
    }

    AnimatedVisibility(
        visible = state.isInputEmailOTP || state.isVerifyOTP || state.isResetPassword,
        enter = fadeIn() + slideInHorizontally(),
        exit = fadeOut() + slideOutHorizontally()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(id = R.string.reset_password),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
            ) {
                Column {
                    AnimatedVisibility(
                        visible = state.isInputEmailOTP,
                        enter = fadeIn() + slideInHorizontally(),
                        exit = fadeOut() + slideOutHorizontally()
                    ) {
                        Column {
                            Text(
                                text = stringResource(id = R.string.enter_your_email),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            AuthTextField(
                                label = R.string.email,
                                state = state,
                                onEmailChanged = onEmailChanged,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    AnimatedVisibility(
                        visible = state.isVerifyOTP,
                        enter = fadeIn() + slideInHorizontally(),
                        exit = fadeOut() + slideOutHorizontally()
                    ) {
                        Column {
                            OTPTextField(
                                value = state.otp,
                                onTextChanged = {
                                    onOTPChanged(it)
                                },
                                numDigits = 6,
                                isMasked = true,
                                digitContainerStyle = OtpTextFieldDefaults.outlinedContainer(),
                                textStyle = MaterialTheme.typography.titleLarge,
                                isError = state.otpError,
                            )
                        }
                    }
                    AnimatedVisibility(
                        visible = state.isResetPassword,
                        enter = fadeIn() + slideInHorizontally(),
                        exit = fadeOut() + slideOutHorizontally()
                    ) {
                        Column {
                            Text(
                                text = stringResource(id = R.string.enter_your_new_password),
                                style = MaterialTheme.typography.titleMedium
                            )
                            AuthTextField(
                                label = R.string.password,
                                state = state,
                                onPasswordChange = onPasswordChange,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            AuthTextField(
                                label = R.string.repassword,
                                state = state,
                                onRePasswordChanged = onRePasswordChanged,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
            AuthButton(
                onClick =
                    when {
                        state.isInputEmailOTP -> onContinueToVerifyOTP
                        state.isVerifyOTP -> onOTPSubmit
                        state.isResetPassword -> onCompleteResetPassword
                        else -> onSignInClick
                    },
                content =
                    when {
                        state.isResetPassword -> R.string.reset_password
                        else -> R.string.continue_to
                    },
                enabled =
                    when {
                        state.isInputEmailOTP -> state.emailError.not()
                        state.isVerifyOTP -> state.otpError.not()
                        state.isResetPassword -> state.passwordError.not() && state.rePasswordError.not()
                        else -> false
                    }
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick =
                    when{
                        state.isResetPassword -> onCancelClick
                        state.isVerifyOTP -> onBackToInputEmail
                        state.isInputEmailOTP -> onBackToSignInClick
                        else -> onCancelClick
                    },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.back))
            }
        }
    }
}

    @Composable
    private fun InputLoginSection(
        loginClick: () -> Unit,
        onUsernameChanged: (String) -> Unit,
        onPasswordChange: (String) -> Unit,
        onForgotPasswordClick: () -> Unit,
        state: AuthState
    ) {
        AuthTextField(
            label = R.string.username,
            state = state,
            onUsernameChanged = onUsernameChanged,
            modifier = Modifier.fillMaxWidth()
        )
        AuthTextField(
            label = R.string.password,
            state = state,
            onPasswordChange = onPasswordChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 0.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {
            Text(
                text = stringResource(id = R.string.forgot_password),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.clickable(onClick = { onForgotPasswordClick() })
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        AuthButton(
            onClick = { loginClick() },
            content = R.string.login,
            enabled = state.username.isNotEmpty()
                    && state.password.isNotEmpty()
                    && state.passwordError.not()
        )
    }


    @Composable
    private fun SocialMediaMethodSection() {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(id = R.string.or_continue_with),
                style = MaterialTheme.typography.labelMedium.copy(color = Color(0xFF64748B))
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SocialMediaLogin(
                    icon = R.drawable.google,
                    text = "Google",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        // TODO
                    }
                )
                Spacer(modifier = Modifier.width(20.dp))
                SocialMediaLogin(
                    icon = R.drawable.facebook,
                    text = "Facebook",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        // TODO
                    }
                )
            }
        }
    }

    @Composable
    private fun BottomSection(
        @StringRes text: Int,
        @StringRes textBold: Int,
        onClick: () -> Unit
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight(fraction = 0.3f)
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                modifier = Modifier.clickable { onClick() },
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF94A3B8),
                            fontSize = 14.sp,
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Normal
                        )
                    ) {
                        append(stringResource(id = text))
                    }
                    withStyle(
                        style = SpanStyle(
                            fontSize = 14.sp,
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Medium
                        ),
                    ) {
                        append(stringResource(id = textBold))
                    }
                }
            )
        }
    }


    @Composable
    private fun UserSignedInContent(
        users: List<UserModel>,
        onChoose: (String) -> Unit,
        onDeleteUser: (String) -> Unit,
        onSwitchToSignInClick: () -> Unit,
        onSwitchToSignUpClick: () -> Unit
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 30.dp)
        ) {
            AnimatedVisibility(
                visible = users.isNotEmpty(),
                enter = fadeIn() + slideInHorizontally(),
                exit = fadeOut() + slideOutHorizontally()
            ) {
                Column(
                    verticalArrangement = Arrangement.Bottom
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(
                                top = 8.dp,
                                end = 8.dp,
                                start = 8.dp
                            ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(users.take(3).size) {
                            UserSignedInItem(
                                onClick = { onChoose(users[it].username) },
                                onDelete = { onDeleteUser(users[it].username) },
                                content = users[it].username
                            )
                        }
                    }

                    UserSignedInBottom(
                        modifier = Modifier
                            .padding(bottom = 30.dp),
                        onSignInAnother = { onSwitchToSignInClick() },
                        onSignUp = { onSwitchToSignUpClick() }
                    )
                }
            }
            AnimatedVisibility(
                visible = users.isEmpty(),
                enter = fadeIn() + slideInHorizontally(),
                exit = fadeOut() + slideOutHorizontally()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.no_account),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(70.dp))
                    UserSignedInBottomButton(
                        content = R.string.login_now,
                        onClick = { onSwitchToSignInClick() },
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = stringResource(id = R.string.dont_have_account),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(30.dp))
                    UserSignedInBottomButton(
                        content = R.string.signup,
                        onClick = { onSwitchToSignUpClick() },
                    )
                }
            }
        }
    }


    @Composable
    fun UserSignedInBottom(
        modifier: Modifier,
        onSignInAnother: () -> Unit,
        onSignUp: () -> Unit
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 30.dp)
                .padding(top = 20.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(id = R.string.or),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    UserSignedInBottomButton(
                        onClick = { onSignInAnother() },
                        content = R.string.another_account
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(id = R.string.dont_have_account),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    UserSignedInBottomButton(
                        onClick = { onSignUp() },
                        content = R.string.signup_now,
                    )
                }
            }
        }
    }


    @Composable
    fun AuthButton(
        onClick: () -> Unit,
        content: Int,
        enabled: Boolean = false
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            shape = Shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSystemInDarkTheme()) BlueGray else Color.Black,
                contentColor = Color.White
            ),
            enabled = enabled
        ) {
            Text(
                text = stringResource(id = content),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
            )
        }
    }


    @Composable
    fun UserSignedInBottomButton(
        content: Int,
        onClick: () -> Unit,
    ) {
        OutlinedButton(
            onClick = { onClick() },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = stringResource(id = content),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }


    @Composable
    fun UserSignedInItem(
        onClick: () -> Unit,
        onDelete: () -> Unit,
        content: String
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .height(40.dp),
                shape = Shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSystemInDarkTheme()) BlueGray else Color.Black,
                    contentColor = Color.White
                ),
            ) {
                Text(
                    text = content,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
                )
            }
            Spacer(modifier = Modifier.fillMaxWidth(0.05f))
            Button(
                onClick = { onDelete() },
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .height(40.dp),
                shape = Shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSystemInDarkTheme()) BlueGray else Color.Black,
                    contentColor = Color.White
                ),
            ) {
                Text(
                    text = stringResource(id = R.string.delete),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
                )
            }
        }

    }