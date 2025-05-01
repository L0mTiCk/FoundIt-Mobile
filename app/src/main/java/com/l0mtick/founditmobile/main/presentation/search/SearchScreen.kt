package com.l0mtick.founditmobile.main.presentation.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.l0mtick.founditmobile.common.presentation.navigation.NavigationRoute
import com.l0mtick.founditmobile.main.presentation.search.components.ListLayout
import com.l0mtick.founditmobile.main.presentation.search.components.MapLayout
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchRoot(
    navController: NavController,
    viewModel: SearchViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SearchScreen(
        state = state,
        onNavigateToDetails = {
            navController.navigate(NavigationRoute.Main.ItemDetails)
        },
        onAction = viewModel::onAction
    )
}

@Composable
fun SearchScreen(
    state: SearchState,
    onNavigateToDetails: () -> Unit,
    onAction: (SearchAction) -> Unit,
) {
    when (state) {
        is SearchState.ListScreen -> ListLayout(state, onAction, onItemClick = { onNavigateToDetails() })
        SearchState.MapScreen -> MapLayout()
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    FoundItMobileTheme {
        SearchScreen(
            state = SearchState.ListScreen(),
            onAction = {},
            onNavigateToDetails = {}
        )
    }
}