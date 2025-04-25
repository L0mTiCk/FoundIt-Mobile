package com.l0mtick.founditmobile.main.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l0mtick.founditmobile.common.domain.error.Result
import com.l0mtick.founditmobile.main.domain.model.User
import com.l0mtick.founditmobile.main.domain.repository.CategoriesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(private val categoriesRepository: CategoriesRepository) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(HomeState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                viewModelScope.launch {
                    launch {
                        when (val result = categoriesRepository.getPopularCategories()) {
                            is Result.Success -> {
                                _state.update { it.copy(categories = result.data) }
                                Log.d("categories", result.data.toString())
                            }

                            is Result.Error -> {
                                Log.e("home_viewmodel", result.toString())
                            }
                        }
                    }

                    launch {
                        delay(2000)
                        val users = listOf(
                                User(1),
                                User(3, levelItemsCount = 7),
                                User(2, level = 10),
                            )
                        _state.update { it.copy(topLevelUsers = users) }
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