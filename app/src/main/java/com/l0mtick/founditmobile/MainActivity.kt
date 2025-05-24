package com.l0mtick.founditmobile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.l0mtick.founditmobile.common.data.notification.NotificationHelper
import com.l0mtick.founditmobile.common.data.snackbar.SnackbarManager
import com.l0mtick.founditmobile.common.presentation.components.CustomSnackbar
import com.l0mtick.founditmobile.common.presentation.components.NoWifiBottomSheet
import com.l0mtick.founditmobile.common.presentation.components.NotificationPermissionDialog
import com.l0mtick.founditmobile.common.presentation.navigation.NavigationRoute
import com.l0mtick.founditmobile.main.presentation.MainEventManager
import com.l0mtick.founditmobile.main.presentation.MainRoot
import com.l0mtick.founditmobile.start.presentation.StartRoot
import com.l0mtick.founditmobile.ui.theme.FoundItMobileTheme
import com.l0mtick.founditmobile.ui.theme.Theme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val viewModel by inject<MainActivityViewModel>()
    private val snackbarManager by inject<SnackbarManager>()
    private lateinit var notificationHelper: NotificationHelper
    
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        viewModel.checkNotificationPermission(isGranted)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        notificationHelper = NotificationHelper(this)
        notificationHelper.createNotificationChannels()
        
        checkNotificationPermission()
        
        // Обрабатываем intent, если приложение запущено из уведомления
        handleNotificationIntent(intent)
        
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.value.isLoading
            }
        }
        enableEdgeToEdge()
        setContent {
            val state = viewModel.state.collectAsState()
            val coroutineScope = rememberCoroutineScope()

            LaunchedEffect(MainEventManager) {
                coroutineScope.launch {
                    MainEventManager.eventsFlow.collect { event ->
                        when (event) {
                            is MainEventManager.MainEvent.OnDarkThemeChanged -> {
                                viewModel.updateDarkTheme()
                            }
                        }
                    }
                }
            }

            FoundItMobileTheme(
                darkTheme = state.value.isDarkTheme
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
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
                    
                    // Показываем диалог запроса разрешений, если нужно
                    if (state.value.shouldShowNotificationPermissionDialog) {
                        NotificationPermissionDialog(
                            onDismiss = { viewModel.hideNotificationPermissionDialog() },
                            onAllowClick = {
                                viewModel.hideNotificationPermissionDialog()
                                requestNotificationPermission()
                            }
                        )
                    }
                    
                    // Проверяем разрешения при изменении состояния подключения
                    LaunchedEffect(state.value.isInternetConnected) {
                        if (state.value.isInternetConnected && !state.value.hasNotificationPermission) {
                            checkNotificationPermission()
                        }
                    }
                    
                    // Глобальный snackbar поверх всего контента
                    CustomSnackbar(
                        snackbarManager = snackbarManager,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNotificationIntent(intent)
    }
    
    private fun handleNotificationIntent(intent: Intent?) {
        intent?.getIntExtra("chat_id", -1)?.let { chatId ->
            if (chatId != -1) {
                // Здесь можно добавить навигацию к чату или другую логику
            }
        }
    }
    
    private fun checkNotificationPermission() {
        val hasPermission = notificationHelper.hasNotificationPermission()
        viewModel.checkNotificationPermission(hasPermission)
        
        if (!hasPermission && !notificationHelper.areNotificationsEnabled()) {
            viewModel.showNotificationPermissionDialog()
        }
    }
    
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationHelper.requestNotificationPermission(requestPermissionLauncher)
        }
    }
}