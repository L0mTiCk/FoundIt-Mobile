package com.l0mtick.founditmobile.start.presentation.login.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.presentation.components.PrimaryButton
import com.l0mtick.founditmobile.common.presentation.components.SecondaryButton
import com.l0mtick.founditmobile.start.presentation.login.LoginAction
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun InitialLogin(
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier
) {
    var showGuestDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 40.dp)
            .then(modifier),
        verticalArrangement = Arrangement.Bottom,
    ) {
        Text(
            text = "Found It",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.app_description),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(24.dp))
        PrimaryButton(
            text = stringResource(R.string.log_in),
            onClick = { onAction(LoginAction.OnMoveToLogin) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.or),
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(12.dp))
        SecondaryButton(
            text = stringResource(R.string.sign_up),
            onClick = { onAction(LoginAction.OnMoveToSignup) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(6.dp))
        TextButton(
            onClick = {
                showGuestDialog = true
            },
            colors = ButtonDefaults.textButtonColors(contentColor = Theme.colors.brand),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.login_as_guest),
                style = Theme.typography.body,
                color = Theme.colors.brand
            )
        }
        Spacer(Modifier.height(12.dp))
    }
    if (showGuestDialog) {
        GuestLoginDialog(
            onDismiss = {
                showGuestDialog = false
            },
            onProceed = {
                onAction(LoginAction.OnGuestLogin)
            }
        )
    }
}

@Composable
private fun GuestLoginDialog(
    onDismiss: () -> Unit,
    onProceed: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Theme.colors.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.guest_login_title),
                    style = Theme.typography.title
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.guest_login_description),
                    style = Theme.typography.body
                )
                Spacer(Modifier.height(12.dp))
                AnimatedVisibility(visible = isLoading) {
                    CircularProgressIndicator(
                        color = Theme.colors.brand,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.align(Alignment.End),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(contentColor = Theme.colors.brand),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            style = Theme.typography.body
                        )
                    }
                    PrimaryButton(
                        onClick = {
                            isLoading = true
                            onProceed()
                        },
                        text = stringResource(R.string.proceed),
                        modifier = Modifier.weight(1.2f)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InitialPreview() {
    FoundItMobileTheme {
        InitialLogin(
            {}
        )
    }
}