package com.l0mtick.founditmobile.main.presentation.lostitemdetails

import com.l0mtick.founditmobile.main.domain.model.LostItem
import com.l0mtick.founditmobile.main.domain.model.User

data class LostItemDetailsState(
    val isGuest: Boolean = false,
    val lostItem: LostItem? = null,
    val owner: User? = null,
)