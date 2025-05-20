package com.l0mtick.founditmobile.main.presentation.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.main.presentation.components.ConfirmationDialog
import com.l0mtick.founditmobile.main.presentation.home.components.SectionHeader
import com.l0mtick.founditmobile.main.presentation.profile.components.ProfileScreenRow
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import com.l0mtick.founditmobile.ui.theme.Theme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsRoot(
    navController: NavController,
    viewModel: SettingsViewModel = koinViewModel(),
    onSignOut: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavBack = { navController.navigateUp() },
        onSignOut = onSignOut
    )
}

@Composable
fun SettingsScreen(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit,
    onNavBack: () -> Unit,
    onSignOut: () -> Unit
) {
    var showSignOutDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .systemBarsPadding(),
        ) {
            IconButton(
                onClick = onNavBack,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Exit settings",
                    tint = Theme.colors.onSurface
                )
            }
            SectionHeader(
                header = R.string.settings_title,
                description = null,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        
        Spacer(Modifier.height(24.dp))
        
        Text(
            text = stringResource(R.string.settings_preferences),
            style = Theme.typography.headline,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        
        Spacer(Modifier.height(16.dp))
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileScreenRow(
                header = stringResource(R.string.settings_dark_theme_header),
                description = stringResource(R.string.settings_dark_theme_description),
                trailingIcon = null,
                isClickable = false,
                onClick = { },
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = state.isDarkTheme,
                onCheckedChange = { onAction(SettingsAction.ToggleTheme) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Theme.colors.brand,
                    checkedTrackColor = Theme.colors.brand.copy(alpha = 0.5f),
                    uncheckedThumbColor = Theme.colors.onSurfaceVariant,
                    uncheckedTrackColor = Theme.colors.onSurfaceVariant.copy(alpha = 0.2f)
                )
            )
        }
        
        Spacer(Modifier.height(8.dp))
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileScreenRow(
                header = stringResource(R.string.settings_notifications_header),
                description = stringResource(R.string.settings_notifications_description),
                isClickable = false,
                onClick = { },
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = state.notificationsEnabled,
                onCheckedChange = { onAction(SettingsAction.ToggleNotifications) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Theme.colors.brand,
                    checkedTrackColor = Theme.colors.brand.copy(alpha = 0.5f),
                    uncheckedThumbColor = Theme.colors.onSurfaceVariant,
                    uncheckedTrackColor = Theme.colors.onSurfaceVariant.copy(alpha = 0.2f)
                )
            )
        }
        
        Spacer(Modifier.height(24.dp))
        
        Text(
            text = stringResource(R.string.settings_account),
            style = Theme.typography.headline,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        
        Spacer(Modifier.height(16.dp))
        
        ProfileScreenRow(
            header = stringResource(R.string.settings_sign_out_header),
            description = stringResource(R.string.settings_sign_out_description),
            trailingIcon = Icons.AutoMirrored.Filled.ExitToApp,
            iconSize = 28.dp,
            onClick = { showSignOutDialog = true },
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        if (showSignOutDialog) {
            ConfirmationDialog(
                title = stringResource(R.string.settings_sign_out_header),
                message = stringResource(R.string.settings_sign_out_confirmation),
                confirmButtonText = stringResource(R.string.exit),
                dismissButtonText = stringResource(R.string.cancel),
                onConfirm = {
                    onAction(SettingsAction.SignOut)
                    onSignOut()
                    showSignOutDialog = false
                },
                onDismiss = { showSignOutDialog = false },
            )
        }

        Spacer(Modifier.height(48.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    FoundItMobileTheme {
        SettingsScreen(
            state = SettingsState(),
            onAction = {},
            onNavBack = {},
            onSignOut = {}
        )
    }
}