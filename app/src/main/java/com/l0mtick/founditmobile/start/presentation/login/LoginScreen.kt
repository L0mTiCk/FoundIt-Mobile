package com.l0mtick.founditmobile.start.presentation.login

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.l0mtick.founditmobile.start.presentation.login.components.InitialLogin
import com.l0mtick.founditmobile.start.presentation.login.components.LogInForm
import com.l0mtick.founditmobile.start.presentation.login.components.SignupForm
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginRoot(
    navController: NavController,
    viewModel: LoginViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LoginScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
) {
    AnimatedContent(
        targetState = state,
        transitionSpec = { fadeIn(tween(400)) togetherWith fadeOut(tween(800)) }
    ) { loginState ->
        when (loginState) {
            LoginState.Initial -> InitialLogin(
                onAction = onAction
            )

            is LoginState.LoginForm -> LogInForm(
                onAction = onAction,
                loginState = loginState.loginState,
                passwordState = loginState.passwordState
            )

            is LoginState.SignupForm -> SignupForm(
                onAction = onAction,
                loginState = loginState.loginState,
                emailState = loginState.emailState,
                passwordState = loginState.passwordState,
                confirmPasswordState = loginState.confirmPasswordState
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    FoundItMobileTheme {
        LoginScreen(
            state = LoginState.Initial,
            onAction = {}
        )
    }
}