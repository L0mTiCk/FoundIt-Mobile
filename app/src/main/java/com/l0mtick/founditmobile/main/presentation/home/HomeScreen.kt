package com.l0mtick.founditmobile.main.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.l0mtick.founditmobile.main.presentation.home.components.CategoryCard
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoot(
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
) {
    Column {
        CategoryCard(
            "First category",
            "http://10.0.2.2:8081/uploads/user_logos/12.jpg"
        )
    }
}

@Preview
@Composable
private fun Preview() {
    FoundItMobileTheme {
        HomeScreen(
            state = HomeState(),
            onAction = {}
        )
    }
}