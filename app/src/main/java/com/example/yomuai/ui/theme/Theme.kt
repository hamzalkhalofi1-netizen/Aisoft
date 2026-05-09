package com.example.yomuai.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = MidnightDarkPrimary,
    background = MidnightDarkBackground,
    surface = MidnightDarkSurface,
)

private val LightColorScheme = lightColorScheme(
    primary = PaperLightPrimary,
    background = PaperLightBackground,
    surface = PaperLightSurface,
)

@Composable
fun YomuAITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}