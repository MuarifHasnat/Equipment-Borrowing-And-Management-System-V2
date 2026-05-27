package com.example.equipmentborrowingapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    secondary = Secondary,
    tertiary = Warning,
    background = ColorDarkSurface,
    surface = ColorDarkSurface,
    onPrimary = TextLight,
    onSecondary = TextLight,
    onTertiary = TextPrimary,
    onBackground = TextLight,
    onSurface = TextLight,
    error = Error
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    tertiary = Warning,
    background = AppBackground,
    surface = SurfaceWhite,
    onPrimary = TextLight,
    onSecondary = TextLight,
    onTertiary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = Error,
    outline = BorderLight
)

@Composable
fun EquipmentBorrowingAppTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}