package com.l0mtick.founditmobile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l0mtick.founditmobile.common.presentation.navigation.NavigationRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainActivityViewModel( /*repository for user token check*/ ): ViewModel() {

    private val _state = MutableStateFlow(MainActivityState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = false,
                    navigationRoute = NavigationRoute.Main.Home
                )
            }
        }
    }

    fun navigateToMain() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    //TODO: change to real screen
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

}

data class MainActivityState(
    val isLoading: Boolean = true,
    val navigationRoute: NavigationRoute = NavigationRoute.Start.Login
)