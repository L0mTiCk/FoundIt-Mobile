package com.l0mtick.founditmobile.main.presentation.inbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.data.snackbar.SnackbarManager
import com.l0mtick.founditmobile.common.data.snackbar.SnackbarType
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.common.presentation.util.UiText
import com.l0mtick.founditmobile.main.domain.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InboxViewModel(
    private val chatRepository: ChatRepository,
    private val snackbarManager: SnackbarManager
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(InboxState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadChats()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = InboxState()
        )

    private fun loadChats() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
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
                    _state.update {
                        it.copy(isLoading = false)
                    }
                    snackbarManager.showSnackbar(
                        UiText.StringResource(R.string.inbox_load_error),
                        SnackbarType.ERROR
                    )
                }
            }
        }
    }

    fun onAction(action: InboxAction) {
        when (action) {
            InboxAction.UpdateChats -> loadChats()
        }
    }

}