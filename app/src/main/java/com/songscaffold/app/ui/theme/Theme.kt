package com.songscaffold.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Blue80,
    onPrimary = BackgroundDark,
    primaryContainer = Blue40,
    onPrimaryContainer = Blue80,
    secondary = BlueGrey80,
    onSecondary = BackgroundDark,
    tertiary = Indigo80,
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onBackground = androidx.compose.ui.graphics.Color.White,
    onSurface = androidx.compose.ui.graphics.Color.White,
    onSurfaceVariant = BlueGrey80
)

@Composable
fun SongScaffoldTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
