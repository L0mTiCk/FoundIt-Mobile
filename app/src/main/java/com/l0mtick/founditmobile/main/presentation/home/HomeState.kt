package com.l0mtick.founditmobile.main.presentation.home

import com.l0mtick.founditmobile.main.domain.model.Category
import com.l0mtick.founditmobile.main.domain.model.User

data class HomeState(
    val categories: List<Category> = emptyList(),
    val topLevelUsers: List<User> = emptyList(),
)