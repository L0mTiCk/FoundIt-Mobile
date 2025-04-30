package com.l0mtick.founditmobile.main.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.main.presentation.profile.components.ProfileScreenRow
import com.l0mtick.founditmobile.main.presentation.profile.components.UserLevelBox
import com.l0mtick.founditmobile.main.presentation.profile.components.UserProfile
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun ProfileRoot(
    viewModel: ProfileViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfileScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit,
) {
    val context = LocalContext.current
    val rowsModifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            UserProfile(
                "",
                "Olivia",
                "oliviasmith@gmail.com",
                modifier = Modifier
                    .systemBarsPadding()
                    .padding(horizontal = 24.dp)

            )
        }
        item {
            UserLevelBox(
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
        item {
            Text(
                text = "Your account",
                style = Theme.typography.headline,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
        item {
            ProfileScreenRow(
                header = "Favorites",
                description = context.resources.getQuantityString(R.plurals.items_count, 3, 3),
                trailingIcon = Icons.Default.FavoriteBorder,
                onClick = { },
                modifier = rowsModifier
            )
            ProfileScreenRow(
                header = "My items",
                description = context.resources.getQuantityString(R.plurals.items_count, 0, 0),
                trailingIcon = Icons.AutoMirrored.Filled.List,
                onClick = { },
                modifier = rowsModifier
            )
            ProfileScreenRow(
                header = "Settings",
                onClick = { },
                trailingIcon = Icons.Default.Settings,
                modifier = rowsModifier
            )
            ProfileScreenRow(
                header = "About",
                onClick = { },
                trailingIcon = Icons.Default.Info,
                modifier = rowsModifier
            )
            ProfileScreenRow(
                header = "Get help",
                trailingIcon = Icons.Default.Call,
                onClick = { },
                modifier = rowsModifier
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    FoundItMobileTheme {
        ProfileScreen(
            state = ProfileState(),
            onAction = {}
        )
    }
}