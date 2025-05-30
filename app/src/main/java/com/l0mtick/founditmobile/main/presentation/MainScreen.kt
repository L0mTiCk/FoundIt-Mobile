package com.l0mtick.founditmobile.main.presentation

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.presentation.components.PrimaryButton
import com.l0mtick.founditmobile.common.presentation.navigation.NavigationRoute
import com.l0mtick.founditmobile.common.presentation.util.DeepLinkConstants.baseUri
import com.l0mtick.founditmobile.common.presentation.util.ObserveAsEvents
import com.l0mtick.founditmobile.main.domain.model.LocationAvailabilityState
import com.l0mtick.founditmobile.main.presentation.additem.AddItemRoot
import com.l0mtick.founditmobile.main.presentation.chat.ChatRoot
import com.l0mtick.founditmobile.main.presentation.home.HomeRoot
import com.l0mtick.founditmobile.main.presentation.inbox.InboxRoot
import com.l0mtick.founditmobile.main.presentation.lostitemdetails.LostItemDetailsRoot
import com.l0mtick.founditmobile.main.presentation.profile.ProfileRoot
import com.l0mtick.founditmobile.main.presentation.search.SearchRoot
import com.l0mtick.founditmobile.main.presentation.settings.SettingsRoot
import com.l0mtick.founditmobile.main.presentation.useritems.UserItemsRoot
import com.l0mtick.founditmobile.ui.theme.Theme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainRoot(
    onNavigateToLogin: () -> Unit = {},
    viewModel: MainScreenViewModel = koinViewModel()
) {

    val uiState = viewModel.uiState.collectAsState().value
    val locationAvailability = uiState.locationAvailability

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissionsResult ->
            viewModel.onPermissionResult(permissionsResult)
            // Check shouldShowRequestPermissionRationale here if needed for permanent denial message
        }
    )

    val gpsEnableLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { activityResult ->
            // Check if the result code indicates success (user likely enabled GPS)
            viewModel.onGpsResult(activityResult.resultCode == Activity.RESULT_OK)
        }
    )

    ObserveAsEvents(
        flow = viewModel.eventChannel
    ) { event ->
        when (event) {
            is LocationUiEvent.RequestPermissions -> {
                viewModel.triggerPermissionRequest(permissionLauncher)
            }

            is LocationUiEvent.RequestGpsEnable -> {
                viewModel.triggerGpsEnableRequest(gpsEnableLauncher)
            }

            is LocationUiEvent.ShowError -> {
                // Handle showing errors, maybe via a Snackbar or another dialog
                // The main blocking dialog is handled by uiState.showLocationDialog
                println("Location Error Event: ${event.error}")
            }
        }

    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        MainScreen(
            onNavigateToLogin = {
                onNavigateToLogin()
                viewModel.endGuestSession()
            }
        )

        if (uiState.showLocationDialog) {
            LocationBlockerDialog(
                availabilityState = locationAvailability,
                onGrantPermissionClick = {
                    viewModel.triggerPermissionRequest(permissionLauncher)
                },
                onEnableGpsClick = {
                    viewModel.triggerGpsEnableRequest(gpsEnableLauncher)
                },
                onCheckPlayServicesClick = {
                    // Optional: Add logic to guide user to update Play Services
                    // For now, just acknowledge
                }
            )
        }
    }
}

@Keep
private enum class MainScreenNavigationItem(
    @Keep
    @StringRes val label: Int,
    @Keep
    @DrawableRes val icon: Int,
    @Keep
    val route: NavigationRoute.Main
) {
    @Keep
    HOME(R.string.navigation_home, R.drawable.home, NavigationRoute.Main.Home),
    @Keep
    SEARCH(R.string.navigation_search, R.drawable.search, NavigationRoute.Main.Search()),
    @Keep
    ADD(R.string.navigation_add, R.drawable.add, NavigationRoute.Main.Add),
    @Keep
    INBOX(R.string.navigation_inbox, R.drawable.inbox, NavigationRoute.Main.Inbox),
    @Keep
    PROFILE(R.string.navigation_profile, R.drawable.profile, NavigationRoute.Main.Profile),
}

@Composable
fun MainScreen(
    onNavigateToLogin: () -> Unit,
) {
    val localNavController = rememberNavController()
    val state = localNavController.currentBackStackEntryAsState().value
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val currentDestination = state?.destination?.route
    Log.d("destination", currentDestination.toString())
    
    val viewModel: MainScreenViewModel = koinViewModel()
    val isGuestUser = viewModel.isGuestUser.collectAsState().value
    
    val navigationItems = if (isGuestUser) {
        listOf(MainScreenNavigationItem.HOME, MainScreenNavigationItem.SEARCH)
    } else {
        MainScreenNavigationItem.entries.toList()
    }

    val customNavSuiteType = with(adaptiveInfo) {
        if (MainScreenNavigationItem.entries.any { currentDestination?.startsWith(it.route::class.qualifiedName ?: "null") ?: false} ) {
            NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
        } else {
            NavigationSuiteType.None
        }
    }
    val barColors = Theme.navBarItemColor
    val railColors = Theme.navRailItemColor
    val drawerColor = Theme.navDrawerItemColor

    NavigationSuiteScaffold(
        containerColor = Theme.colors.background,
        modifier = Modifier.fillMaxSize(),
        layoutType = customNavSuiteType,
        navigationSuiteItems = {
            navigationItems.forEach { route ->
                item(
                    label = { Text(stringResource(route.label), style = Theme.typography.small) },
                    onClick = {
                        localNavController.navigate(route.route) {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(painterResource(route.icon), contentDescription = "") },
                    selected = currentDestination?.startsWith(route.route::class.qualifiedName ?: "null")
                        ?: false,
                    colors = NavigationSuiteItemColors(
                        navigationBarItemColors = barColors,
                        navigationRailItemColors = railColors,
                        navigationDrawerItemColors = drawerColor
                    )

                )

            }
        },
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContainerColor = Theme.colors.surface,
            navigationRailContainerColor = Theme.colors.surface,
            navigationDrawerContainerColor = Theme.colors.surface,
        ),
        content = {
            NavHost(
                navController = localNavController,
                startDestination = NavigationRoute.Main.Home,
                modifier = Modifier.fillMaxSize(),
            ) {
                composable<NavigationRoute.Main.Home> {
                    HomeRoot(
                        isGuest = isGuestUser,
                        onMoveToLogin = onNavigateToLogin,
                        navController = localNavController
                    )
                }

                composable<NavigationRoute.Main.Search> {
                    SearchRoot(
                        navController = localNavController
                    )
                }

                composable<NavigationRoute.Main.Inbox> {
                    InboxRoot(localNavController)
                }

                composable<NavigationRoute.Main.Profile> {
                    ProfileRoot(
                        navController = localNavController
                    )
                }

                composable<NavigationRoute.Main.ItemDetails> {
                    LostItemDetailsRoot(
                        navController = localNavController
                    )
                }

                composable<NavigationRoute.Main.Chat>(
                    deepLinks = listOf(
                        navDeepLink<NavigationRoute.Main.Chat>(
                            basePath = "$baseUri/chat/{chatId}"
                        )
                    )
                ) {
                    ChatRoot(
                        navController = localNavController
                    )
                }
                
                composable<NavigationRoute.Main.Settings> {
                    SettingsRoot(
                        navController = localNavController,
                        onSignOut = onNavigateToLogin
                    )
                }

                composable<NavigationRoute.Main.Add> {
                    AddItemRoot(
                        onNavBack = {
                            localNavController.navigateUp()
                        }
                    )
                }

                composable<NavigationRoute.Main.UserItems>(
                    deepLinks = listOf(
                        navDeepLink<NavigationRoute.Main.UserItems>(
                            basePath = "$baseUri/useritems?isFavorite={isFavorite}"
                        )
                    )
                ) {
                    UserItemsRoot(
                        navController = localNavController
                    )
                }
            }
        }
    )
}

@Composable
fun LocationBlockerDialog(
    availabilityState: LocationAvailabilityState,
    onGrantPermissionClick: () -> Unit,
    onEnableGpsClick: () -> Unit,
    onCheckPlayServicesClick: () -> Unit
) {
    val title: String
    val text: String
    val buttonText: String
    val onButtonClick: () -> Unit
    val context = LocalContext.current
    val activity = context as? Activity

    when {
        !availabilityState.arePlayServicesAvailable -> {
            title = stringResource(R.string.dialog_play_services_unavailable_title)
            text = stringResource(R.string.dialog_play_services_unavailable_text)
            buttonText = stringResource(R.string.dialog_play_services_unavailable_button_text)
            onButtonClick = onCheckPlayServicesClick
        }
//        !availabilityState.hasLocationPermission &&
//                activity != null &&
//                !ActivityCompat.shouldShowRequestPermissionRationale(
//                    activity,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) -> {
//            title = stringResource(R.string.dialog_location_permission_permanently_denied_title)
//            text = stringResource(R.string.dialog_location_permission_permanently_denied_text)
//            buttonText = stringResource(R.string.dialog_location_permission_permanently_denied_button_text)
//            onButtonClick = {
//                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
//                    data = Uri.fromParts("package", context.packageName, null)
//                }
//                context.startActivity(intent)
//            }
//        }

        !availabilityState.hasLocationPermission -> {
            title = stringResource(R.string.dialog_location_permission_required_title)
            text = stringResource(R.string.dialog_location_permission_required_text)
            buttonText = stringResource(R.string.dialog_location_permission_required_button_text)
            onButtonClick = onGrantPermissionClick
        }
        !availabilityState.isGpsEnabled -> {
            title = stringResource(R.string.dialog_gps_disabled_title)
            text = stringResource(R.string.dialog_gps_disabled_text)
            buttonText = stringResource(R.string.dialog_gps_disabled_button_text)
            onButtonClick = onEnableGpsClick
        }
        else -> {
            title = stringResource(R.string.dialog_location_unavailable_title)
            text = stringResource(R.string.dialog_location_unavailable_text)
            buttonText = stringResource(R.string.dialog_location_unavailable_button_text)
            onButtonClick = {}
        }
    }

    Dialog(
        onDismissRequest = {
            // --- Back Press Handling ---
            activity?.finish()
        },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Theme.colors.surface
            )
        ) { // Using Card for standard dialog appearance
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = Theme.typography.title,
                    color = Theme.colors.onSurface
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = text,
                    style = Theme.typography.body,
                    color = Theme.colors.onSurface
                )
                Spacer(Modifier.height(16.dp))
                PrimaryButton(
                    onClick = onButtonClick,
                    modifier = Modifier.align(Alignment.End),
                    text = buttonText
                )
            }
        }
    }
}