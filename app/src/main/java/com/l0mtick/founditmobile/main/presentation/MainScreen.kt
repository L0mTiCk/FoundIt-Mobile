package com.l0mtick.founditmobile.main.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.l0mtick.founditmobile.common.presentation.navigation.NavigationRoute
import com.l0mtick.founditmobile.main.presentation.home.HomeRoot
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun MainRoot(
    onNavigateToLogin: () -> Unit = {}
) {
    MainScreen (
        onNavigateToLogin = onNavigateToLogin
    )
}

@Composable
fun MainScreen(
    onNavigateToLogin: () -> Unit
) {
    val localNavController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        containerColor = Theme.colors.background
    ) { scaffoldPadding ->
        NavHost(
            navController = localNavController,
            startDestination = NavigationRoute.Main.Home,
            modifier = Modifier.padding(scaffoldPadding)
        ) {
            composable<NavigationRoute.Main.Home> {
                HomeRoot()
            }
        }
    }
}