package com.l0mtick.founditmobile.main.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.presentation.components.defaultPlaceholder
import com.l0mtick.founditmobile.common.presentation.navigation.NavigationRoute
import com.l0mtick.founditmobile.main.presentation.profile.components.AboutDialog
import com.l0mtick.founditmobile.main.presentation.profile.components.ProfileScreenRow
import com.l0mtick.founditmobile.main.presentation.profile.components.UserLevelBox
import com.l0mtick.founditmobile.main.presentation.profile.components.UserProfile
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import com.l0mtick.founditmobile.ui.theme.Theme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileRoot(
    navController: NavController,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfileScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigateToSettings = {
            navController.navigate(NavigationRoute.Main.Settings)
        },
        onNavigateToMy = {
            navController.navigate(NavigationRoute.Main.UserItems(isFavorite = false))
        },
        onNavigateToFavorites = {
            navController.navigate(NavigationRoute.Main.UserItems(isFavorite = true))
        }
    )
}

@Composable
fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToMy: () -> Unit,
    onNavigateToFavorites: () -> Unit,
) {
    val context = LocalContext.current
    val rowsModifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)

    var isAboutShown by remember { mutableStateOf(false) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            UserProfile(
                user = state.user,
                modifier = Modifier
                    .systemBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 5.dp)

            )
        }
        item {
            UserLevelBox(
                level = state.user?.level ?: 1,
                items = state.user?.levelItemsCount ?: 0,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .defaultPlaceholder(visible = state.user == null)
            )
        }
        item {
            Text(
                text = stringResource(R.string.profile_your_account),
                style = Theme.typography.headline,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
        item {
            ProfileScreenRow(
                header = stringResource(R.string.profile_favorites),
                description = context.resources.getQuantityString(
                    R
                        .plurals.items_count,
                    state.favoriteCount,
                    state.favoriteCount
                ),
                trailingIcon = Icons.Default.FavoriteBorder,
                onClick = onNavigateToFavorites,
                modifier = rowsModifier
            )
            ProfileScreenRow(
                header = stringResource(R.string.profile_my_items),
                description = context.resources.getQuantityString(
                    R.plurals.items_count,
                    state.user?.levelItemsCount ?: 0,
                    state.user?.levelItemsCount ?: 0
                ),
                trailingIcon = Icons.AutoMirrored.Filled.List,
                onClick = onNavigateToMy,
                modifier = rowsModifier
            )
            ProfileScreenRow(
                header = stringResource(R.string.profile_settings),
                onClick = onNavigateToSettings,
                trailingIcon = Icons.Default.Settings,
                modifier = rowsModifier
            )
            ProfileScreenRow(
                header = stringResource(R.string.profile_about),
                onClick = {
                    isAboutShown = true
                },
                trailingIcon = Icons.Default.Info,
                modifier = rowsModifier
            )
        }
    }
    if (isAboutShown) {
        AboutDialog(
            onDismiss = { isAboutShown = false}
        )
    }
}

@Preview
@Composable
private fun Preview() {
    FoundItMobileTheme {
        ProfileScreen(
            state = ProfileState(),
            onAction = {},
            onNavigateToSettings = {},
            onNavigateToMy = {},
            onNavigateToFavorites = {}
        )
    }
}