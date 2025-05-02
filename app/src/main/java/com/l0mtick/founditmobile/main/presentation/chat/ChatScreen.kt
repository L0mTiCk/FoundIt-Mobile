package com.l0mtick.founditmobile.main.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.l0mtick.founditmobile.main.presentation.chat.components.ChatHeader
import com.l0mtick.founditmobile.main.presentation.chat.components.ChatMessage
import com.l0mtick.founditmobile.main.presentation.chat.components.ItemChatHeader
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import com.l0mtick.founditmobile.ui.theme.Theme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatRoot(
    viewModel: ChatViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ChatScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ChatScreen(
    state: ChatState,
    onAction: (ChatAction) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        stickyHeader {
            Column (
                modifier = Modifier.background(Theme.colors.background).systemBarsPadding()
            ) {
                ChatHeader(
                    username = "ammmm_v",
                    logoUrl = null,
                    onBackPressed = {}
                )
                ItemChatHeader(
                    imageUrl = "",
                    header = "Black backpack at the corner of shop",
                    description = "Take it take it Take it take it Take it take it",
                    status = "Active",
                    modifier = Modifier.padding(12.dp)
                )
                HorizontalDivider(
                    color = Theme.colors.onSurfaceVariant,
                    thickness = 0.2.dp
                )
            }
        }
        item {
            Spacer(Modifier.height(12.dp))
        }
        repeat(10) {
            item {
                ChatMessage(
                    message = "Test message from me to you, not very long",
                    sentDate = "11.22 AM",
                    isFromMe = true,
                    username = null,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(12.dp))
            }
            item {
                ChatMessage(
                    message = "Test message from me to you, very long Test message from me to you, very long Test message from me to you, very long",
                    sentDate = "11.22 AM",
                    isFromMe = false,
                    username = null,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    FoundItMobileTheme {
        ChatScreen(
            state = ChatState(),
            onAction = {}
        )
    }
}