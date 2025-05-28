package com.l0mtick.founditmobile.main.presentation.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.data.snackbar.SnackbarManager
import com.l0mtick.founditmobile.common.data.snackbar.SnackbarType
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.common.presentation.util.UiText
import com.l0mtick.founditmobile.main.domain.repository.UsersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val usersRepository: UsersRepository,
    private val snackbarManager: SnackbarManager
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ProfileState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                init()
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
            is ProfileAction.ProfilePictureSelected -> uploadProfilePicture(action.uri)
            ProfileAction.RemoveProfilePictureClicked -> removeProfilePicture()
        }
    }

    private fun init() {
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
                        snackbarManager.showSnackbar(
                            UiText.StringResource(R.string.profile_loading_error),
                            SnackbarType.ERROR
                        )
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
                        snackbarManager.showSnackbar(
                            UiText.StringResource(R.string.profile_loading_error),
                            SnackbarType.ERROR
                        )
                    }
                }
            }
        }
    }

    private fun uploadProfilePicture(uri: Uri) {
        viewModelScope.launch {
            when(usersRepository.updateUserProfilePicture(uri)) {
                is Result.Success -> {
                    snackbarManager.showSuccess(
                        UiText.StringResource(R.string.profile_picture_change_success)
                    )
                    _state.update {
                        it.copy(
                            user = null
                        )
                    }
                    init()
                }
                is Result.Error -> {
                    snackbarManager.showSuccess(
                        UiText.StringResource(R.string.profile_picture_change_error)
                    )
                }
            }
        }
    }

    private fun removeProfilePicture() {
        viewModelScope.launch {
            when(usersRepository.deleteUserProfilePicture()) {
                is Result.Success -> {
                    snackbarManager.showSuccess(
                        UiText.StringResource(R.string.profile_picture_change_success)
                    )
                    _state.update {
                        it.copy(
                            user = null
                        )
                    }
                    init()
                }
                is Result.Error -> {
                    snackbarManager.showSuccess(
                        UiText.StringResource(R.string.profile_picture_change_error)
                    )
                }
            }
        }
    }

}