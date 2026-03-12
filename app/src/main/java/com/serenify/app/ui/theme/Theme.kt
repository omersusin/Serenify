package com.serenify.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val SerenifyColorScheme = darkColorScheme(
    primary = Accent,
    onPrimary = TextWhite,
    primaryContainer = AccentSubtle,
    secondary = Blue,
    onSecondary = TextWhite,
    tertiary = Green,
    background = BgPrimary,
    onBackground = TextWhite,
    surface = BgSecondary,
    onSurface = TextWhite,
    surfaceVariant = BgElevated,
    onSurfaceVariant = TextGray,
    outline = Border
)

@Composable
fun SerenifyTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = BgPrimary.toArgb()
            window.navigationBarColor = BgPrimary.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = SerenifyColorScheme,
        typography = SerenifyTypography,
        content = content
    )
}
