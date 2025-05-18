package com.l0mtick.founditmobile.main.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.presentation.navigation.NavigationRoute
import com.l0mtick.founditmobile.main.presentation.home.components.CategoryGrid
import com.l0mtick.founditmobile.main.presentation.home.components.SectionHeader
import com.l0mtick.founditmobile.main.presentation.home.components.TopLevelUsersRow
import com.l0mtick.founditmobile.main.presentation.home.components.UserHeaderCard
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoot(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        state = state,
        onAction = viewModel::onAction,
        onCategoryClick = { id ->
            navController.navigate(NavigationRoute.Main.Search(listOf(id)))
        }
    )
}

@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    onCategoryClick: (Long) -> Unit
) {
    LazyColumn (
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            UserHeaderCard(
                username = state.localUser.username,
                profilePictureUrl = state.localUser.profilePictureUrl,
                modifier = Modifier.padding(horizontal = 24.dp).systemBarsPadding()
            )
        }
        item {
            SectionHeader(
                header = R.string.popular_category,
                description = null,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
        item {
            CategoryGrid(
                categories = state.categories,
                modifier = Modifier.padding(horizontal = 24.dp),
                onCategoryClick = onCategoryClick
            )
        }
        item {
            SectionHeader(
                header = R.string.top_level_users,
                description = R.string.top_level_users_description,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
        item {
            TopLevelUsersRow(
                users = state.topLevelUsers,
                onUserCardClick = { /*TODO*/ }
            )
        }
        item {
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Preview
@Composable
private fun Preview() {
    FoundItMobileTheme {
        HomeScreen(
            state = HomeState(),
            onAction = {},
            onCategoryClick = {}
        )
    }
}