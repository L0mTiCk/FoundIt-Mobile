package com.l0mtick.founditmobile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.common.domain.repository.ConnectivityObserver
import com.l0mtick.founditmobile.common.presentation.navigation.NavigationRoute
import com.l0mtick.founditmobile.start.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val authRepository: AuthRepository,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _state = MutableStateFlow(MainActivityState())
    val state = _state.asStateFlow()

    init {
        observeConnectivity()
        checkAuth()
    }
    
    fun checkNotificationPermission(hasPermission: Boolean) {
        _state.update {
            it.copy(hasNotificationPermission = hasPermission)
        }
    }
    
    fun showNotificationPermissionDialog() {
        _state.update {
            it.copy(shouldShowNotificationPermissionDialog = true)
        }
    }
    
    fun hideNotificationPermissionDialog() {
        _state.update {
            it.copy(shouldShowNotificationPermissionDialog = false)
        }
    }

    private fun checkAuth() {
        viewModelScope.launch {
            val result = authRepository.checkToken()
            when (result) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            navigationRoute = NavigationRoute.Main.Home
                        )
                    }
                }

                is Result.Error<*, *> -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            navigationRoute = NavigationRoute.Start.Login
                        )
                    }
                }
            }
        }
    }

    fun navigateToMain() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    navigationRoute = NavigationRoute.Main.Home
                )
            }
        }
    }

    fun navigateToLogin() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    navigationRoute = NavigationRoute.Start.Login
                )
            }
        }
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            var isConnectedOnLaunch = connectivityObserver.isConnected.first()
            connectivityObserver.isConnected.collect { isConnected ->
                _state.update {
                    it.copy(
                        isInternetConnected = isConnected,
                        isTryingToLogIn = (!isConnectedOnLaunch && isConnected)
                    )
                }
                if (!isConnectedOnLaunch && isConnected) {
                    delay(1500)
                    checkAuth()
                    isConnectedOnLaunch = true
                    _state.update {
                        it.copy(isTryingToLogIn = false)
                    }
                }
            }
        }
    }
}

data class MainActivityState(
    val isLoading: Boolean = true,
    val navigationRoute: NavigationRoute = NavigationRoute.Start.Login,
    val isInternetConnected: Boolean = true,
    val shouldShowNotificationPermissionDialog: Boolean = false,
    val hasNotificationPermission: Boolean = false,
    val isTryingToLogIn: Boolean = false
)