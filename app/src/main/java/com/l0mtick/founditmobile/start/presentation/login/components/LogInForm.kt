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
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.l0mtick.founditmobile.common.presentation.util.TextFieldState
import com.l0mtick.founditmobile.common.presentation.util.asUiText
import com.l0mtick.founditmobile.start.presentation.login.LoginAction

@Composable
fun LogInForm(
    onAction: (LoginAction) -> Unit,
    loginState: TextFieldState,
    passwordState: TextFieldState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 40.dp),
        verticalArrangement = Arrangement.Bottom,
    ) {
        OutlinedTextField(
            label = { Text("Login or Email") },
            modifier = Modifier.fillMaxWidth(),
            value = loginState.value,
            onValueChange = {
                onAction(LoginAction.LoginFormAction.OnLoginChanged(it))
            },
            isError = loginState.isError,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            supportingText = { Text(loginState.errors.firstOrNull()?.asUiText()?.asString() ?: "") }
        )
        OutlinedTextField(
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            value = passwordState.value,
            onValueChange = {
                onAction(LoginAction.LoginFormAction.OnPasswordChanged(it))
            },
            isError = passwordState.isError,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password),
            supportingText = { Text(passwordState.errors.firstOrNull()?.asUiText()?.asString() ?: "") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                onAction(LoginAction.LoginFormAction.OnSubmit)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log In")
        }
        Spacer(Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                text = "or",
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(12.dp))
        OutlinedButton(
            onClick = { onAction(LoginAction.OnMoveToSignup) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginFormPreview() {
    LogInForm(
        onAction = {},
        loginState = TextFieldState(),
        passwordState = TextFieldState()
    )
}