package com.l0mtick.founditmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.l0mtick.founditmobile.common.presentation.navigation.NavigationRoute
import com.l0mtick.founditmobile.start.presentation.StartRoot
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
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
                val state = viewModel.state.collectAsState()
                when (state.value.navigationRoute) {
                    is NavigationRoute.Main -> {
                        Text("Main screen")
                    }
                    is NavigationRoute.Start -> {
                        StartRoot(
                            onNavigateToMain = {
                                viewModel.navigateToMain()
                            }
                        )
                    }
                }
            }
        }
    }
}