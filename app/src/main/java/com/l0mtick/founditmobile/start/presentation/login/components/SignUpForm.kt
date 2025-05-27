package com.l0mtick.founditmobile.start.presentation.login.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.presentation.components.LoadingPrimaryButton
import com.l0mtick.founditmobile.common.presentation.components.OutlinedAppTextField
import com.l0mtick.founditmobile.common.presentation.components.SecondaryButton
import com.l0mtick.founditmobile.common.presentation.util.TextFieldState
import com.l0mtick.founditmobile.common.presentation.util.asUiText
import com.l0mtick.founditmobile.start.presentation.login.LoginAction
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun SignupForm(
    onAction: (LoginAction) -> Unit,
    loginState: TextFieldState,
    emailState: TextFieldState,
    passwordState: TextFieldState,
    confirmPasswordState: TextFieldState,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 40.dp)
            .then(modifier),
        verticalArrangement = Arrangement.Bottom,
    ) {
        OutlinedAppTextField(
            label = stringResource(R.string.username),
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentType = ContentType.NewUsername
                },
            value = loginState.value,
            onValueChange = {
                onAction(LoginAction.SignupFormAction.OnUsernameChanged(it))
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            isError = loginState.isError,
            errorText = loginState.errors.firstOrNull()?.asUiText()?.asString() ?: ""
        )
        OutlinedAppTextField(
            label = stringResource(R.string.email),
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentType = ContentType.EmailAddress
                },
            value = emailState.value,
            onValueChange = {
                onAction(LoginAction.SignupFormAction.OnEmailChanged(it))
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            isError = emailState.isError,
            errorText = emailState.errors.firstOrNull()?.asUiText()?.asString() ?: ""
        )
        OutlinedAppTextField(
            label = stringResource(R.string.password),
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentType = ContentType.NewPassword
                },
            value = passwordState.value,
            onValueChange = {
                onAction(LoginAction.SignupFormAction.OnPasswordChanged(it))
            },
            isError = passwordState.isError,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password
            ),
            errorText = passwordState.errors.firstOrNull()?.asUiText()?.asString() ?: "",
            visualTransformation = PasswordVisualTransformation()
        )
        OutlinedAppTextField(
            label = stringResource(R.string.confirm_password),
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentType = ContentType.NewPassword
                },
            value = confirmPasswordState.value,
            onValueChange = {
                onAction(LoginAction.SignupFormAction.OnConfirmPasswordChanged(it))
            },
            isError = confirmPasswordState.isError,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            errorText = confirmPasswordState.errors.firstOrNull()?.asUiText()?.asString() ?: "",
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(24.dp))
        LoadingPrimaryButton(
            text = stringResource(R.string.sign_up),
            isLoading = isLoading,
            onClick = {
                onAction(LoginAction.SignupFormAction.OnSubmit)
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.or),
                modifier = Modifier.padding(horizontal = 12.dp),
                color = Theme.colors.onSurface,
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(12.dp))
        SecondaryButton(
            text = stringResource(R.string.log_in),
            onClick = { onAction(LoginAction.OnMoveToLogin) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SignpFormPreview() {
    SignupForm(
        onAction = {},
        loginState = TextFieldState(),
        emailState = TextFieldState(),
        passwordState = TextFieldState(),
        confirmPasswordState = TextFieldState(),
        isLoading = true,
    )
}