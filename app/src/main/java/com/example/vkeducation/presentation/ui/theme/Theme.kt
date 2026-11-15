package com.example.vkeducation.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1967D2),
    onPrimary = Color.White,
    background = Color(0xFFF4F6FA),
    onBackground = Color(0xFF182230),
    surface = Color.White,
    onSurface = Color(0xFF182230),
    onSurfaceVariant = Color(0xFF536174)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFA9C7FF),
    onPrimary = Color(0xFF00316B),
    background = Color(0xFF10151E),
    onBackground = Color(0xFFE3E9F3),
    surface = Color(0xFF1A2331),
    onSurface = Color(0xFFE3E9F3),
    onSurfaceVariant = Color(0xFFB7C4D6)
)

@Composable
fun VKEDUCATIONTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
