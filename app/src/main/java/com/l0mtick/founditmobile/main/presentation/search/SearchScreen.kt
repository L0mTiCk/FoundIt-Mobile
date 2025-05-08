package com.l0mtick.founditmobile.main.presentation.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.l0mtick.founditmobile.common.presentation.navigation.NavigationRoute
import com.l0mtick.founditmobile.main.presentation.components.FloatingModeSwitchButton
import com.l0mtick.founditmobile.main.presentation.search.components.ErrorLayout
import com.l0mtick.founditmobile.main.presentation.search.components.ListLayout
import com.l0mtick.founditmobile.main.presentation.search.components.LoadingLayout
import com.l0mtick.founditmobile.main.presentation.search.components.MapLayout
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchRoot(
    navController: NavController,
    viewModel: SearchViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

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
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (state) {
            is SearchState.Loading -> {
                LoadingLayout(state = state)
            }
            is SearchState.Error -> {
                ErrorLayout(state = state)
            }
            is SearchState.ListScreen -> ListLayout(state, onAction, onItemClick = { onNavigateToDetails() })
            is SearchState.MapScreen -> MapLayout(state)
        }
        if (state is SearchState.MapScreen || state is SearchState.ListScreen) {
            FloatingModeSwitchButton(
                text = "Switch mode",
                onClick = {
                    onAction(SearchAction.OnModeChange)
                },
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 12.dp)
            )
        }
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