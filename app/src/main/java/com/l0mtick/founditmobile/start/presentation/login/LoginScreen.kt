package com.l0mtick.founditmobile.start.presentation.login

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
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
        targetState = state.screenType,
        transitionSpec = { fadeIn(tween(400)) togetherWith fadeOut(tween(800)) using SizeTransform(clip = false) }
    ) { screenType ->
        when (screenType) {
            LoginScreenType.Initial -> InitialLogin(
                onAction = onAction
            )

            LoginScreenType.Login -> {
                val loginState = state as? LoginState.LoginForm ?: LoginState.LoginForm()
                LogInForm(
                    onAction = onAction,
                    loginState = loginState.loginState,
                    passwordState = loginState.passwordState
                )
            }

            LoginScreenType.Signup -> {
                val loginState = state as? LoginState.SignupForm ?: LoginState.SignupForm()
                SignupForm(
                    onAction = onAction,
                    loginState = loginState.loginState,
                    emailState = loginState.emailState,
                    passwordState = loginState.passwordState,
                    confirmPasswordState = loginState.confirmPasswordState
                )
            }
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