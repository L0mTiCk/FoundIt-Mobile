package com.l0mtick.founditmobile.main.presentation.profile

import com.l0mtick.founditmobile.main.domain.model.User

data class ProfileState(
    val user: User? = null,
    val favoriteCount: Int = 0,
)