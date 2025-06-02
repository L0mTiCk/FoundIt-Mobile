package com.l0mtick.founditmobile.common.data.snackbar

import com.l0mtick.founditmobile.common.domain.error.Error
import com.l0mtick.founditmobile.common.presentation.util.UiText
import com.l0mtick.founditmobile.common.presentation.util.asUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SnackbarData(
    val message: UiText,
    val type: SnackbarType = SnackbarType.INFO,
    val duration: Long = 3000L
)

enum class SnackbarType {
    SUCCESS,
    ERROR,
    WARNING,
    INFO
}

class SnackbarManager {
    private val _snackbarState = MutableStateFlow<SnackbarData?>(null)
    val snackbarState: StateFlow<SnackbarData?> = _snackbarState.asStateFlow()

    fun showSnackbar(
        message: UiText,
        type: SnackbarType = SnackbarType.INFO,
        duration: Long = 3000L
    ) {
        _snackbarState.value = SnackbarData(
            message = message,
            type = type,
            duration = duration
        )
    }

    fun showSnackbar(
        message: String,
        type: SnackbarType = SnackbarType.INFO,
        duration: Long = 3000L
    ) {
        showSnackbar(
            message = UiText.DynamicString(message),
            type = type,
            duration = duration
        )
    }

    fun showError(error: Error) {
        showSnackbar(
            message = error.asUiText(),
            type = SnackbarType.ERROR
        )
    }

    fun showSuccess(message: UiText) {
        showSnackbar(
            message = message,
            type = SnackbarType.SUCCESS
        )
    }

    fun showSuccess(message: String) {
        showSnackbar(
            message = UiText.DynamicString(message),
            type = SnackbarType.SUCCESS
        )
    }

    fun hideSnackbar() {
        _snackbarState.value = null
    }

    companion object {
        @Volatile
        private var INSTANCE: SnackbarManager? = null

        fun getInstance(): SnackbarManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SnackbarManager().also { INSTANCE = it }
            }
        }
    }
}