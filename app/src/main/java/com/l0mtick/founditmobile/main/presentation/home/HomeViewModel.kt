package com.l0mtick.founditmobile.main.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.main.domain.repository.CategoriesRepository
import com.l0mtick.founditmobile.main.domain.repository.UsersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val categoriesRepository: CategoriesRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(HomeState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                viewModelScope.launch {
                    launch {
                        when(val result = usersRepository.getLocalMe()) {
                            is Result.Success-> {
                                _state.update {
                                    it.copy(
                                        localUser = result.data
                                    )
                                }
                            }
                            is Result.Error -> {
                                Log.e("home_viewmodel", "No local user")
                            }
                        }
                    }
                    launch {
                        when (val result = categoriesRepository.getPopularCategories()) {
                            is Result.Success -> {
                                _state.update { it.copy(categories = result.data) }
                            }

                            is Result.Error -> {
                                Log.e("home_viewmodel", result.toString())
                            }
                        }
                    }

                    launch {
                        when (val result = usersRepository.getTopLevelUsers()) {
                            is Result.Success -> {
                                _state.update { it.copy(topLevelUsers = result.data ?: emptyList()) }
                            }

                            is Result.Error -> {
                                Log.e("home_viewmodel", result.toString())
                            }
                        }
                    }
                }
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HomeState()
        )

    fun onAction(action: HomeAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

}