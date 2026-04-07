package com.krishisetu.ui.theme

import androidx.compose.ui.graphics.toArgb
import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary             = Green800,
    onPrimary           = White,
    primaryContainer    = Green100,
    onPrimaryContainer  = Green900,
    secondary           = Green600,
    onSecondary         = White,
    secondaryContainer  = Green50,
    onSecondaryContainer = Green800,
    background          = SurfaceWhite,
    onBackground        = Black,
    surface             = SurfaceGreenTint,
    onSurface           = Black,
    surfaceVariant      = Gray100,
    onSurfaceVariant    = Gray600,
    outline             = Gray200,
    error               = ErrorRed,
)

private val DarkColors = darkColorScheme(
    primary             = Green500,
    onPrimary           = Green900,
    primaryContainer    = Green800,
    onPrimaryContainer  = Green50,
    secondary           = Green400,
    onSecondary         = Green900,
    background          = Color(0xFF0D1510),
    onBackground        = White,
    surface             = Color(0xFF111A14),
    onSurface           = White,
    surfaceVariant      = Color(0xFF1E2D22),
    onSurfaceVariant    = Gray400,
    outline             = Color(0xFF2E3D32),
    error               = Color(0xFFFF6B6B),
)

@Composable
fun KrishiSetuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = KrishiTypography,
        content     = content
    )
}