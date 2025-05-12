package com.l0mtick.founditmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.l0mtick.founditmobile.common.presentation.components.NoWifiBottomSheet
import com.l0mtick.founditmobile.common.presentation.navigation.NavigationRoute
import com.l0mtick.founditmobile.main.presentation.MainRoot
import com.l0mtick.founditmobile.start.presentation.StartRoot
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import com.l0mtick.founditmobile.ui.theme.Theme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val viewModel by inject<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.value.isLoading
            }
        }
        enableEdgeToEdge()
        setContent {
            FoundItMobileTheme {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val state = viewModel.state.collectAsState()
                    when (state.value.navigationRoute) {
                        is NavigationRoute.Main -> {
                            MainRoot(
                                onNavigateToLogin = {
                                    viewModel.navigateToLogin()
                                }
                            )
                        }

                        is NavigationRoute.Start -> {
                            StartRoot(
                                onNavigateToMain = {
                                    viewModel.navigateToMain()
                                }
                            )
                        }
                    }
                    if (!state.value.isInternetConnected) {
                        NoWifiBottomSheet()
                    }
                    if (state.value.isTryingToLogIn) {
                        CircularProgressIndicator(
                            color = Theme.colors.brand,
                            modifier = Modifier
                                .size(48.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}