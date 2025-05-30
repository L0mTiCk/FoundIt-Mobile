package com.l0mtick.founditmobile.common.presentation.navigation

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
sealed interface NavigationRoute {

    @Keep
    @Serializable
    sealed interface Start : NavigationRoute {

        @Keep
        @Serializable
        data object Introduction : Start

        @Keep
        @Serializable
        data object Login : Start

        @Keep
        @Serializable
        data class PhoneVerification(val login: String, val email: String, val pass: String) : Start

    }

    @Keep
    @Serializable
    sealed interface Main : NavigationRoute {

        @Keep
        @Serializable
        data object Home : Main

        @Keep
        @Serializable
        data class Search(val categoryIds: List<Long>? = null) : Main

        @Keep
        @Serializable
        data object Add : Main

        @Keep
        @Serializable
        data object Inbox : Main

        @Keep
        @Serializable
        data object Profile : Main

        @Keep
        @Serializable
        data object Settings : Main

        @Keep
        @Serializable
        data class ItemDetails(val itemId: Int) : Main

        @Keep
        @Serializable
        data class Chat(val chatId: Int) : Main

        @Keep
        @Serializable
        data class UserItems(val isFavorite: Boolean) : Main

    }

}

