package com.amir.notes.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = LightTextColorPrimary,
    secondary = LightBackgroundGradientEnd,
    background = LightBackgroundGradientStart,
    surface = LightGlassBg,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = LightTextColorPrimary,
    onSurface = LightTextColorPrimary,
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkTextColorPrimary,
    secondary = DarkBackgroundGradientEnd,
    background = DarkBackgroundGradientStart,
    surface = DarkGlassBg,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = DarkTextColorPrimary,
    onSurface = DarkTextColorPrimary,
)

@Composable
fun NotesTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography, 
        content = content
    )
}
