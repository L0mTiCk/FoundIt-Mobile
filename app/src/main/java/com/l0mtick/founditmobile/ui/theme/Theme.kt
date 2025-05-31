package com.l0mtick.founditmobile.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationDrawerItemColors
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRailItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val baseLightColors = FoundItColors(
    brand = Color(0xFF397a57),
    brandMuted = Color(0xFFD7E4DD),
    onBrand = Color.White,
    secondary = Color(0xFFBDC5BE),
    onSecondary = Color(0xFF002200),
    background = Color(0xFFf8f9fa),
    onBackground = Color(0xFF171A1F),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF171A1F),
    onSurfaceVariant = Color(0xFF5F6877),
    text = Color(0xFF0D141C),
    statusPending = Color(0xFFD3A42B),
    statusExpired = Color(0xFFB00020).copy(alpha = 0.7f),
    statusFound = Color(0xFF2196F3).copy(alpha = 0.6f)
)

val baseDarkColors = baseLightColors.copy(
    background = Color(0xFF171A1F),
    brandMuted = Color(0xFFD7E4DD),
    surface = Color(0xFF1D2026),
    onSurface = Color(0xFFFFFFFF),
    onSurfaceVariant = Color(0xFF9A9FB2),
    statusPending = Color(0xFFE6C25F),
    statusExpired = Color(0xFFCF6679),
    statusFound = Color(0xFF8AB4F8)
)

val baseTypography = FoundItTypography(
    headline = TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight.Medium
    ),
    title = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium
    ),
    body = TextStyle(
        fontSize = 16.sp,
    ),
    description = TextStyle(
        fontSize = 14.sp,
    ),
    small = TextStyle(
        fontSize = 12.sp,
    )
)

@Composable
fun FoundItMobileTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {

    val colors = if (darkTheme) baseDarkColors else baseLightColors
    val typography = baseTypography


    MaterialTheme(
        content = {
            CompositionLocalProvider(
                LocalColors provides colors,
                LocalTypography provides typography,
                content = content
            )
        }
    )
}

object Theme {
    val colors: FoundItColors
        @ReadOnlyComposable @Composable
        get() = LocalColors.current

    val typography: FoundItTypography
        @ReadOnlyComposable @Composable
        get() = LocalTypography.current

    val navRailItemColor: NavigationRailItemColors
        @ReadOnlyComposable @Composable
        get() = NavigationRailItemColors(
        selectedIconColor = colors.brand,
        selectedTextColor = colors.onSurface,
        selectedIndicatorColor = colors.brand.copy(alpha = .2f),
        unselectedIconColor = colors.onSurface,
        unselectedTextColor = colors.onSurface,
        disabledIconColor = colors.onSurface,
        disabledTextColor = colors.onSurface,
    )

    val navBarItemColor: NavigationBarItemColors
        @ReadOnlyComposable @Composable
        get() = NavigationBarItemColors(
            selectedIconColor = colors.brand,
            selectedTextColor = colors.onSurface,
            selectedIndicatorColor = colors.brand.copy(alpha = .2f),
            unselectedIconColor = colors.onSurface,
            unselectedTextColor = colors.onSurface,
            disabledIconColor = colors.onSurface,
            disabledTextColor = colors.onSurface,
        )

    val navDrawerItemColor: NavigationDrawerItemColors
       @Composable
        get() = NavigationDrawerItemDefaults.colors(
            selectedIconColor = colors.brand,
            selectedTextColor = colors.onSurface,
            selectedContainerColor = colors.brand.copy(alpha = .2f),
            unselectedIconColor = colors.onSurface,
            unselectedTextColor = colors.onSurface,
            unselectedContainerColor = colors.surface
        )
}

val LocalColors = staticCompositionLocalOf<FoundItColors> {
    error("No MyColors provided")
}

val LocalTypography = staticCompositionLocalOf<FoundItTypography> {
    error("No MyColors provided")
}