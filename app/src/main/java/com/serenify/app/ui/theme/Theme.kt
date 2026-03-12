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
    primary = VividPurple,
    onPrimary = TextPrimary,
    primaryContainer = SurfaceCard,
    secondary = ElectricBlue,
    onSecondary = TextPrimary,
    tertiary = CyanGlow,
    background = DarkNavy,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = GlassBorder
)

@Composable
fun SerenifyTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DarkNavy.toArgb()
            window.navigationBarColor = DarkNavy.toArgb()
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
