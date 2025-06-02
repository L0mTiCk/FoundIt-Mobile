package com.l0mtick.founditmobile.main.presentation.inbox

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import kotlinx.coroutines.launch
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxScreen(
    state: InboxState,
    onAction: (InboxAction) -> Unit,
    onNavToChat: (chatIt: Int) -> Unit
) {
    val pullToRefreshState = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
    PullToRefreshBox(
        isRefreshing = state.isLoading,
        state = pullToRefreshState,
        onRefresh = {
            coroutineScope.launch {
                if (!state.isLoading) {
                    onAction(InboxAction.UpdateChats)
                }
                pullToRefreshState.animateToHidden()
            }
        },
        indicator = {
            PullToRefreshDefaults.Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                state = pullToRefreshState,
                isRefreshing = state.isLoading,
                containerColor = Theme.colors.brandMuted,
                color = Theme.colors.brand
            )
        }
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
                        modifier = Modifier.fillParentMaxSize(),
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
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.no_chats),
                                style = Theme.typography.headline,
                                color = Theme.colors.onSurface
                            )
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