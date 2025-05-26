package com.l0mtick.founditmobile.main.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.main.presentation.chat.components.ChatHeader
import com.l0mtick.founditmobile.main.presentation.chat.components.ChatMessage
import com.l0mtick.founditmobile.main.presentation.chat.components.ItemChatHeader
import com.l0mtick.founditmobile.main.presentation.chat.components.MessageInput
import com.l0mtick.founditmobile.main.presentation.util.formatChatTimestamp
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import com.l0mtick.founditmobile.ui.theme.Theme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatRoot(
    navController: NavController,
    viewModel: ChatViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ChatScreen(
        state = state,
        onBackPressed = {
            navController.navigateUp()
        },
        onAction = viewModel::onAction
    )
}

@Composable
fun ChatScreen(
    state: ChatState,
    onBackPressed: () -> Unit,
    onAction: (ChatAction) -> Unit,
) {
    val messageInputHeight = 72.dp
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier
        .fillMaxSize()
        .systemBarsPadding()) {
        if (state.isLoading || state.itemTitle.isEmpty()) {
            CircularProgressIndicator(
                color = Theme.colors.brand,
                strokeWidth = 2.dp,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LaunchedEffect(state.messages.size) {
                if (state.messages.isNotEmpty()) {
                    listState.animateScrollToItem(state.messages.lastIndex)
                }
            }
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = messageInputHeight)
            ) {
                stickyHeader {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Theme.colors.background)
                    ) {
                        ChatHeader(
                            username = state.interlocutor?.username ?: "",
                            logoUrl = state.interlocutor?.profilePictureUrl,
                            onBackPressed = onBackPressed
                        )
                        ItemChatHeader(
                            imageUrl = state.itemPictureUrl,
                            header = state.itemTitle,
                            description = state.itemDescription,
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
                if (state.messages.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Column(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .fillParentMaxHeight(0.6f)
                                    .fillMaxWidth(0.7f),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.empty_chat_title),
                                    style = Theme.typography.title
                                )
                                Spacer(Modifier.height(12.dp))
                                Text(
                                    text = stringResource(R.string.empty_chat_description),
                                    style = Theme.typography.body,
                                    color = Theme.colors.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
                items(state.messages) { message ->
                    ChatMessage(
                        message = message.content,
                        sentDate = formatChatTimestamp(message.createdAt),
                        isFromMe = message.senderId != state.interlocutor?.id,
                        username = state.interlocutor?.username,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        }

        MessageInput(
            value = state.messageInput,
            onValueChange = { newValue ->
                onAction(ChatAction.UpdateMessageInput(newValue))
            },
            onSendClick = {
                onAction(ChatAction.SendMessage)
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Theme.colors.background)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    FoundItMobileTheme {
        ChatScreen(
            state = ChatState(),
            onBackPressed = {},
            onAction = {}
        )
    }
}