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
import com.l0mtick.founditmobile.main.presentation.search.components.ErrorDialog
import com.l0mtick.founditmobile.main.presentation.search.components.ListLayout
import com.l0mtick.founditmobile.main.presentation.search.components.LoadingDialog
import com.l0mtick.founditmobile.main.presentation.search.components.MapLayout
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchRoot(
    navController: NavController,
    viewModel: SearchViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val events = viewModel.events

    SearchScreen(
        state = state,
        events = events,
        onNavigateToDetails = { id ->
            navController.navigate(NavigationRoute.Main.ItemDetails(id))
        },
        onAction = viewModel::onAction
    )
}

@Composable
fun SearchScreen(
    state: SearchState,
    onNavigateToDetails: (Int) -> Unit,
    onAction: (SearchAction) -> Unit,
    events: Flow<SearchEvent>,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (state) {
            is SearchState.ListScreen -> {
                ListLayout(
                    state = state,
                    onAction = onAction,
                    onItemClick = onNavigateToDetails
                )
                
                // Show loading dialog if needed
                if (state.isLoading && state.loadingStep != null) {
                    LoadingDialog(step = state.loadingStep)
                }
                
                // Show error dialog if needed
                if (state.error != null) {
                    ErrorDialog(
                        message = state.error,
                        onRetry = { onAction(SearchAction.OnRetry) },
                        onDismissRequest = { onAction(SearchAction.OnRemoveError) }
                    )
                }
            }

            is SearchState.MapScreen -> {
                MapLayout(
                    state = state,
                    events = events,
                    onNavigateToDetails = onNavigateToDetails,
                    onAction = onAction
                )
                
                // Show loading dialog if needed
                if (state.isLoading && state.loadingStep != null) {
                    LoadingDialog(step = state.loadingStep)
                }
                
                // Show error dialog if needed
                if (state.error != null) {
                    ErrorDialog(
                        message = state.error,
                        onRetry = { onAction(SearchAction.OnRetry) },
                        onDismissRequest = { onAction(SearchAction.OnRemoveError) }
                    )
                }
            }
        }
        
        FloatingModeSwitchButton(
            text = "Switch mode",
            onClick = {
                onAction(SearchAction.OnModeChange)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    FoundItMobileTheme {
        SearchScreen(
            state = SearchState.ListScreen(),
            onAction = {},
            onNavigateToDetails = {},
            events = flowOf()
        )
    }
}