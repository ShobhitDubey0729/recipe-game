package com.example.rasoifood.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Saffron = Color(0xFFE65100)
private val Cream = Color(0xFFFFF8E1)
private val DeepGreen = Color(0xFF2E7D32)

private val LightColors = lightColorScheme(
    primary = Saffron,
    secondary = DeepGreen,
    background = Cream,
    surface = Color.White,
)

private val DarkColors = darkColorScheme(
    primary = Saffron,
    secondary = DeepGreen,
)

@Composable
fun RasoiRoyaleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
