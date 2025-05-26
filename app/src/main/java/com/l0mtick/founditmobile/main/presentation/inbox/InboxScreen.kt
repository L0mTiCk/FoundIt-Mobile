package com.l0mtick.founditmobile.main.presentation.inbox

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.presentation.navigation.NavigationRoute
import com.l0mtick.founditmobile.main.presentation.home.components.SectionHeader
import com.l0mtick.founditmobile.main.presentation.inbox.components.ChatCard
import com.l0mtick.founditmobile.main.presentation.util.formatRelativeTime
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import com.l0mtick.founditmobile.ui.theme.Theme
import org.koin.androidx.compose.koinViewModel

@Composable
fun InboxRoot(
    navController: NavController,
    viewModel: InboxViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    InboxScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavToChat = { chatId ->
            navController.navigate(
                NavigationRoute.Main.Chat(
                    chatId = chatId,
                )
            )
        }
    )
}

@Composable
fun InboxScreen(
    state: InboxState,
    onAction: (InboxAction) -> Unit,
    onNavToChat: (chatIt: Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            SectionHeader(
                header = R.string.navigation_inbox,
                description = null,
                modifier = Modifier
                    .systemBarsPadding()
                    .padding(top = 12.dp, bottom = 10.dp)
            )
        }
        if (state.isLoading) {
            item {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Theme.colors.brand
                    )
                }
            }
        } else {
            if (state.chats.isNullOrEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No chats")
                    }
                }
            } else {
                items(
                    items = state.chats,
                    key = { it.id }
                ) { chat ->
                    ChatCard(
                        itemImageUrl = chat.ownerItemImageUrl,
                        userImageUrl = chat.interlocutor.profilePictureUrl,
                        username = chat.interlocutor.username,
                        itemHeader = chat.ownerItemTitle,
                        lastMessage = chat.lastMessage ?: "-----",
                        messageDate = formatRelativeTime(chat.lastMessageAt),
                        { onNavToChat(chat.id) },
                        Modifier.padding(vertical = 6.dp, horizontal = 20.dp),
                    )
                }
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
            onNavToChat = { _ ->
            }
        )
    }
}