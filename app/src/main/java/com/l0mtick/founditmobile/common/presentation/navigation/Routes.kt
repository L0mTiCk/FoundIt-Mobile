package com.l0mtick.founditmobile.common.presentation.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed interface NavigationRoute {

    @Serializable
    sealed interface Start: NavigationRoute {

        @Serializable
        data object Introduction: Start

        @Serializable
        data object Login: Start

        @Serializable
        data class PhoneVerification(val login: String, val email: String, val pass: String): Start

    }

    @Serializable
    sealed interface Main: NavigationRoute {

        @Serializable
        data object Map: Main

    }

}

