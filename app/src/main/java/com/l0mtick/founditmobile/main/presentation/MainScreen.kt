package com.l0mtick.founditmobile.main.presentation

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.l0mtick.founditmobile.R
import com.l0mtick.founditmobile.common.presentation.navigation.NavigationRoute
import com.l0mtick.founditmobile.main.presentation.chat.ChatRoot
import com.l0mtick.founditmobile.main.presentation.home.HomeRoot
import com.l0mtick.founditmobile.main.presentation.inbox.InboxRoot
import com.l0mtick.founditmobile.main.presentation.lostitemdetails.LostItemDetailsRoot
import com.l0mtick.founditmobile.main.presentation.profile.ProfileRoot
import com.l0mtick.founditmobile.main.presentation.search.SearchRoot
import com.l0mtick.founditmobile.ui.theme.Theme

@Composable
fun MainRoot(
    onNavigateToLogin: () -> Unit = {}
) {
    MainScreen(
        onNavigateToLogin = onNavigateToLogin
    )
}

private enum class MainScreenNavigationItem(
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
    val route: NavigationRoute.Main
) {
    HOME(R.string.navigation_home, R.drawable.home, NavigationRoute.Main.Home),
    SEARCH(R.string.navigation_search, R.drawable.search, NavigationRoute.Main.Search),
    ADD(R.string.navigation_add, R.drawable.add, NavigationRoute.Main.Add),
    INBOX(R.string.navigation_inbox, R.drawable.inbox, NavigationRoute.Main.Inbox),
    PROFILE(R.string.navigation_profile, R.drawable.profile, NavigationRoute.Main.Profile),
}

@Composable
fun MainScreen(
    onNavigateToLogin: () -> Unit
) {
    val localNavController = rememberNavController()
    val state = localNavController.currentBackStackEntryAsState().value
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val currentDestination = state?.destination?.route
    Log.d("destination", currentDestination.toString())

    val customNavSuiteType = with(adaptiveInfo) {
        if (MainScreenNavigationItem.entries.any {currentDestination == it.route::class.qualifiedName}) {
            NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
        } else {
            NavigationSuiteType.None
        }
    }
    
    NavigationSuiteScaffold(
        containerColor = Theme.colors.background,
        modifier = Modifier.fillMaxSize(),
        layoutType = customNavSuiteType,
        navigationSuiteItems = {
            MainScreenNavigationItem.entries.forEach { route ->
                item(
                    label = { Text(stringResource(route.label), style = Theme.typography.small) },
                    onClick = {
                        localNavController.navigate(route.route) {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(painterResource(route.icon), contentDescription = "") },
                    selected = currentDestination?.let { route.route::class.qualifiedName == it }
                        ?: false
                )
            }
        },
        content = {
            NavHost(
                navController = localNavController,
                startDestination = NavigationRoute.Main.Home,
                modifier = Modifier.fillMaxSize(),
            ) {
                composable<NavigationRoute.Main.Home> {
                    HomeRoot()
                }

                composable<NavigationRoute.Main.Search> {
                    SearchRoot(
                        navController = localNavController
                    )
                }

                composable<NavigationRoute.Main.Add> { }

                composable<NavigationRoute.Main.Inbox> {
                    InboxRoot(localNavController)
                }

                composable<NavigationRoute.Main.Profile> {
                    ProfileRoot()
                }

                composable<NavigationRoute.Main.ItemDetails> {
                    LostItemDetailsRoot()
                }

                composable<NavigationRoute.Main.Chat> {
                    ChatRoot()
                }
            }
        }
    )
}