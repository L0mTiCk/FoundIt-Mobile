package com.l0mtick.founditmobile.common.presentation.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed interface NavigationRoute {

    @Serializable
    sealed interface Start: NavigationRoute {

        @Serializable
        data object Login: Start

    }

    @Serializable
    sealed interface Main: NavigationRoute {

        @Serializable
        data object Map: Main

    }

}

