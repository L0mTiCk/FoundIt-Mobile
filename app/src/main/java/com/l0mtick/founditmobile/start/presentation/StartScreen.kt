package com.l0mtick.founditmobile.start.presentation
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.l0mtick.founditmobile.common.presentation.navigation.NavigationRoute
import com.l0mtick.founditmobile.start.presentation.introduction.IntroductionRoot
import com.l0mtick.founditmobile.start.presentation.login.LoginRoot
import com.l0mtick.founditmobile.start.presentation.phoneverify.PhoneVerificationRoot
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import com.l0mtick.founditmobile.ui.theme.Theme
import kotlinx.coroutines.launch

@Composable
fun StartRoot(
    onNavigateToMain: () -> Unit = {}
) {
    StartScreen(
        onNavigateToMain = onNavigateToMain
    )
}

@Composable
fun StartScreen(
    onNavigateToMain: () -> Unit
) {
    val localNavController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(StartEventManager) {
        coroutineScope.launch {
            StartEventManager.eventsFlow.collect{ event ->
                when(event) {
                    StartEventManager.StartEvent.OnNavigateToMain -> onNavigateToMain()
                }
            }
        }
    }

    Scaffold(
        containerColor = Theme.colors.background,
    ) { scaffoldPadding ->
        NavHost(
            navController = localNavController,
            startDestination = NavigationRoute.Start.Login,
            modifier = Modifier.padding(scaffoldPadding)
        ) {
            composable<NavigationRoute.Start.Introduction> {
                IntroductionRoot(navController = localNavController)
            }

            composable<NavigationRoute.Start.Login> {
                LoginRoot(navController = localNavController)
            }

            composable<NavigationRoute.Start.PhoneVerification> {
                PhoneVerificationRoot(navController = localNavController)
            }
        }
    }

}

@Preview
@Composable
private fun Preview() {
    FoundItMobileTheme {
        StartScreen(
            onNavigateToMain = {}
        )
    }
}