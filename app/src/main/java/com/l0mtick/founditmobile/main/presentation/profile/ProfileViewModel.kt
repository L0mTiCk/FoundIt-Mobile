package com.l0mtick.founditmobile.main.presentation.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.main.domain.repository.UsersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(private val usersRepository: UsersRepository) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ProfileState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                viewModelScope.launch {
                    launch {
                        when(val result = usersRepository.getMe()) {
                            is Result.Success -> {
                                _state.update {
                                    it.copy(
                                        user = result.data
                                    )
                                }
                            }
                            is Result.Error -> {
                                Log.e("profile_viewmodel", result.toString())
                            }
                        }
                    }
                    launch {
                        when(val result = usersRepository.getMyFavoriteCount()) {
                            is Result.Success -> {
                                _state.update {
                                    it.copy(
                                        favoriteCount = result.data
                                    )
                                }
                            }
                            is Result.Error -> {
                                Log.e("profile_viewmodel", result.toString())
                            }
                        }
                    }
                }
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ProfileState()
        )

    fun onAction(action: ProfileAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

}