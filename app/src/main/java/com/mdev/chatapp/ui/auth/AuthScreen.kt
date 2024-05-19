package com.mdev.chatapp.ui.auth

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mdev.chatapp.R
import com.mdev.chatapp.data.local.user.UserSignedInModel
import com.mdev.chatapp.ui.auth.event_state.AuthResult
import com.mdev.chatapp.ui.auth.common.AuthTextField
import com.mdev.chatapp.ui.auth.common.SocialMediaLogin
import com.mdev.chatapp.ui.auth.event_state.AuthUiEvent
import com.mdev.chatapp.ui.navgraph.Route
import com.mdev.chatapp.ui.theme.BlueGray
import com.mdev.chatapp.ui.theme.Roboto
import com.mdev.chatapp.ui.theme.Shapes

@Composable
fun AuthScreen(
    title: Int,
    content: String,
    viewModel: AuthViewModel = hiltViewModel(),
    navController: NavController,
) {
    val users = viewModel.users.collectAsState(initial = emptyList())

    val context = LocalContext.current
    LaunchedEffect(viewModel, context) {
        viewModel.uiEvent.collect {
            when (it) {
                is AuthResult.Authorized -> {
                    navController.navigate(Route.HomeNavigator.route) {
                        popUpTo(Route.AuthScreen.route) {
                            inclusive = true
                        }
                    }
                }
                is AuthResult.Unauthorized -> {
                    Toast.makeText(
                        context,
                        it.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
                is AuthResult.Error -> {
                    Toast.makeText(
                        context,
                        it.message,
                        Toast.LENGTH_LONG
                    ).show()
                }

                is AuthResult.UnknownError -> {
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
        Column(modifier = Modifier.fillMaxSize()) {
            TopSection(header = title)
            Spacer(modifier = Modifier.height(36.dp))
            when (content) {
                Route.Login.route -> LoginContent(
                    switchToSignUpClicked = { navController.navigate(Route.Signup.route) },
                    loginClick = { viewModel.onEvent(AuthUiEvent.SignIn) }
                )
                Route.Signup.route -> SignupContent(
                    onSwitchToLoginClick = { navController.navigate(Route.Login.route) },
                    onSignUp = { viewModel.onEvent(AuthUiEvent.SignUp) }
                )
                Route.UserSignedIn.route -> UserSignedInContent(
                    users = users.value,
                    onChoose = { username ->
                        viewModel.onEvent(AuthUiEvent.SignedInUsernameChanged(username))
                        viewModel.onEvent(AuthUiEvent.SignedIn)
                    },
                    onDeleteUser = { username ->
                        viewModel.onEvent(AuthUiEvent.SignedInUsernameChanged(username))
                        viewModel.onEvent(AuthUiEvent.UnAuthenticatedUserChanged(username))
                    },
                    onSwitchToSignInClick = { navController.navigate(Route.Login.route) },
                    onSwitchToSignUpClick = { navController.navigate(Route.Signup.route) }
                )
            }
        }
    }
}
@Composable
private fun TopSection(@StringRes header: Int) {
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
fun SignupContent(
    onSwitchToLoginClick: () -> Unit,
    onSignUp: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
    ) {
        InputSignupSection(onSignUpClick = { onSignUp() })
        BottomSection(
            text = R.string.have_account,
            textBold = R.string.login_now,
            onClick = { onSwitchToLoginClick() }
        )
    }
}

@Composable
private fun InputSignupSection(onSignUpClick: () -> Unit) {
    AuthTextField(label = R.string.username_signup, trailing = "", modifier = Modifier.fillMaxWidth())
    Spacer(modifier = Modifier.height(15.dp))

    AuthTextField(label = R.string.email, trailing = "", modifier = Modifier.fillMaxWidth())
    Spacer(modifier = Modifier.height(15.dp))

    AuthTextField(label = R.string.password_signup, trailing = "", modifier = Modifier.fillMaxWidth())
    Spacer(modifier = Modifier.height(15.dp))

    AuthTextField(label = R.string.repassword, trailing = "", modifier = Modifier.fillMaxWidth())
    Spacer(modifier = Modifier.height(20.dp))

    AuthButton(onClick = { onSignUpClick() }, content = R.string.signup)

}


@Composable
fun LoginContent(
    switchToSignUpClicked: () -> Unit,
    loginClick: () ->Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
    ) {
        InputLoginSection(loginClick = { loginClick() })
        Spacer(modifier = Modifier.height(30.dp))
        SocialMediaMethodSection()
        BottomSection(
            text = R.string.dont_have_account,
            textBold = R.string.signup_now,
            onClick = { switchToSignUpClicked() }
        )
    }
}

@Composable
private fun InputLoginSection(loginClick: () -> Unit) {
    AuthTextField(
        label =  R.string.username_login,
        trailing = "",
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(15.dp))

    AuthTextField(
        label = R.string.password,
        trailing = stringResource(id = R.string.forgot),
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(20.dp))
    AuthButton(onClick = { loginClick() }, content = R.string.login)
}


@Composable
private fun SocialMediaMethodSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Or continue with",
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
                onClick = { TODO() }
            )
            Spacer(modifier = Modifier.width(20.dp))
            SocialMediaLogin(
                icon = R.drawable.facebook,
                text = "Facebook",
                modifier = Modifier.weight(1f),
                onClick = { TODO() }
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
@Preview (showBackground = true)
private fun AuthScreenPreview() {
    UserSignedInContent(
        users = listOf(
            UserSignedInModel("user1"),
            UserSignedInModel("user2"),
            UserSignedInModel("user3"),
        ),
        onChoose = {},
        onDeleteUser = {},
        onSwitchToSignInClick = {},
        onSwitchToSignUpClick = {}
    )
}

@Composable
private fun UserSignedInContent(
    users: List<UserSignedInModel>,
    onChoose: (String) -> Unit,
    onDeleteUser: (String) -> Unit,
    onSwitchToSignInClick: () -> Unit,
    onSwitchToSignUpClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 30.dp)
    ) {
        AnimatedVisibility(visible = users.isNotEmpty()) {
            Column(
                verticalArrangement = Arrangement.Bottom
            ){
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
        AnimatedVisibility(visible = users.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No user signed in",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(30.dp))
                UserSignedInBottomButton(
                    content = R.string.login_now,
                    onClick = { onSwitchToSignInClick() },
                )
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "Or",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(30.dp))
                UserSignedInBottomButton(
                    content = R.string.signup_now,
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
){
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
                    text = "Or",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                UserSignedInBottomButton(
                    onClick = { onSignInAnother() },
                    content = R.string.another_account
                )
                Text(
                    text = "Or",
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
    content: Int
){
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
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = stringResource(id = content),
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
    }
}


@Composable
fun UserSignedInItem(
    onClick: () -> Unit,
    onDelete: () -> Unit,
    content: String
){
    Row (
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
                text = "Delete",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
            )
        }
    }

}