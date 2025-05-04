package com.l0mtick.founditmobile.main.presentation.inbox

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.main.domain.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InboxViewModel(private val chatRepository: ChatRepository) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(InboxState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                viewModelScope.launch {
                    when (val result = chatRepository.getAllCurrentUserChats()) {
                        is Result.Success -> {
                            _state.update {
                                it.copy(
                                    chats = result.data,
                                    isLoading = false
                                )
                            }
                        }

                        is Result.Error -> {
                            Log.e("chat_viewmodel", result.toString())
                        }
                    }
                    hasLoadedInitialData = true
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = InboxState()
        )

    fun onAction(action: InboxAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

}