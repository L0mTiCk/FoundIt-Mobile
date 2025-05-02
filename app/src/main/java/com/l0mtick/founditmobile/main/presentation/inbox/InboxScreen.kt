package com.l0mtick.founditmobile.main.presentation.inbox

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.presentation.navigation.NavigationRoute
import com.l0mtick.founditmobile.main.presentation.home.components.SectionHeader
import com.l0mtick.founditmobile.main.presentation.inbox.components.ChatCard
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun InboxRoot(
    navController: NavController,
    viewModel: InboxViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    InboxScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavToChat = { navController.navigate(NavigationRoute.Main.Chat) }
    )
}

@Composable
fun InboxScreen(
    state: InboxState,
    onAction: (InboxAction) -> Unit,
    onNavToChat: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            SectionHeader(
                header = R.string.app_name,
                description = null,
                modifier = Modifier
                    .systemBarsPadding()
                    .padding(top = 12.dp, bottom = 10.dp)
            )
        }
        repeat(11) {
            item {
                ChatCard(
                    "",
                    "",
                    "ammmm_v",
                    "Black backpack at corner shop",
                    "So will you take it take it take it take it it take it it take it",
                    "15.05.2025",
                    { onNavToChat() },
                    Modifier.padding(vertical = 6.dp, horizontal = 20.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    FoundItMobileTheme {
        InboxScreen(
            state = InboxState(),
            onAction = {},
            onNavToChat = {}
        )
    }
}