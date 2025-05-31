package com.l0mtick.founditmobile.main.presentation.chat

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.data.snackbar.SnackbarManager
import com.l0mtick.founditmobile.common.data.snackbar.SnackbarType
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.common.presentation.navigation.NavigationRoute
import com.l0mtick.founditmobile.common.presentation.util.UiText
import com.l0mtick.founditmobile.main.data.remote.websocket.ChatWebSocketClient
import com.l0mtick.founditmobile.main.domain.repository.ChatRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val snackbarManager: SnackbarManager,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val route = savedStateHandle.toRoute<NavigationRoute.Main.Chat>()

    private var hasLoadedInitialData = false

    private val eventChannel = Channel<ChatEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(ChatState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadMessages()
                chatRepository.getWebSocketConnectionState()
                    .onEach { connectionState ->
                        _state.update {
                            it.copy(
                                isConnected = connectionState == ChatWebSocketClient.ConnectionState.CONNECTED
                            )
                        }
                        Log.d("chat_viewmodel", "Wen socket connection state: $connectionState")
                    }
                    .launchIn(viewModelScope)

                chatRepository.getIncomingMessages()
                    .onEach { message ->
                        _state.update { currentState ->
                            currentState.copy(
                                messages = currentState.messages + message
                            )
                        }
                        Log.d("chat_viewmodel", "New message from websocket: $message")
                    }
                    .launchIn(viewModelScope)
                viewModelScope.launch {
                    chatRepository.connectToWebSocket()
                }
                hasLoadedInitialData = true

            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ChatState()
        )

    fun onAction(action: ChatAction) {
        when (action) {
            is ChatAction.LoadMessages -> loadMessages()
            is ChatAction.SendMessage -> sendMessage()
            is ChatAction.UpdateMessageInput -> updateMessageInput(action.text)
            ChatAction.DeleteChat -> deleteChat()
        }
    }

    private fun loadMessages() {
        val currentChatId = route.chatId
        if (currentChatId == -1) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = chatRepository.getChatData(currentChatId)) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            chatId = currentChatId,
                            itemTitle = result.data.itemTitle,
                            itemDescription = result.data.itemDescription,
                            itemPictureUrl = result.data.itemPictureUrl,
                            interlocutor = result.data.interlocutor,
                            messages = result.data.messages,
                            isLoading = false
                        )
                    }
                }

                is Result.Error -> {
                    Log.e("ChatViewModel", "Failed to load messages: ${result.error}")
                    _state.update { it.copy(isLoading = false) }
                    snackbarManager.showError(result.error)
                }
            }
        }
    }

    private fun sendMessage() {
        val currentChatId = _state.value.chatId
        if (currentChatId == -1 || _state.value.messageInput.isBlank()) return

        viewModelScope.launch {
            _state.update { it.copy(isSending = true) }
            Log.d("chat_viewmodel", "Trying to send message for chat id $currentChatId, with content ${_state.value.messageInput}")
            when (val result = chatRepository.sendMessage(currentChatId, _state.value.messageInput.trim())) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            messageInput = "",
                            isSending = false
                        )
                    }
//                    loadMessages()
                }
                is Result.Error -> {
                    Log.e("ChatViewModel", "Failed to send message: ${result.error}")
                    _state.update { it.copy(isSending = false) }
                    snackbarManager.showSnackbar(
                        UiText.StringResource(R.string.chat_send_error),
                        SnackbarType.ERROR
                    )
                }
            }
        }
    }

    private fun updateMessageInput(text: String) {
        _state.update { it.copy(messageInput = text) }
    }

    private fun deleteChat() {
        if (_state.value.chatId == -1) return
        viewModelScope.launch {
            when(chatRepository.deleteChat(_state.value.chatId)) {
                is Result.Success -> {
                    snackbarManager.showSuccess(
                        UiText.StringResource(R.string.delete_chat_success)
                    )
                    eventChannel.send(ChatEvent.NavigateToInbox)
                }
                is Result.Error<*, *> -> {
                    snackbarManager.showSnackbar(
                        UiText.StringResource(R.string.delete_chat_error),
                        SnackbarType.ERROR
                    )
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            chatRepository.disconnectFromWebSocket()
        }
    }

}