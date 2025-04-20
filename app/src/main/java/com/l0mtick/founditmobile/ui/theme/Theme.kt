package com.l0mtick.founditmobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
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
    onBrand = Color.White,
    secondary = Color(0xFFBDC5BE),
    onSecondary = Color(0xFF002200),
    background = Color(0xFFf8f9fa),
    onBackground = Color(0xFF171A1F),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF171A1F),
    onSurfaceVariant = Color(0xFF565E6C),
    text = Color(0xFF0D141C)
)

//TODO: replace with actual dark theme
val baseDarkColors = baseLightColors

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
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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
}

val LocalColors = staticCompositionLocalOf<FoundItColors> {
    error("No MyColors provided")
}

val LocalTypography = staticCompositionLocalOf<FoundItTypography> {
    error("No MyColors provided")
}