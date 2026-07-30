package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = KidsDarkPrimary,
    secondary = KidsDarkSecondary,
    tertiary = KidsDarkTertiary,
    background = KidsDarkBackground,
    surface = KidsDarkSurface,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = KidsPrimary,
    secondary = KidsSecondary,
    tertiary = KidsTertiary,
    background = KidsBackground,
    surface = KidsSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A),
    surfaceContainer = Color(0xFFF1F5F9)
)

@Composable
fun AksharaWordQuestTheme(
    darkTheme: Boolean = false, // Force light theme as requested
    dynamicColor: Boolean = false, // Keep tailored vibrant colors for educational brand consistency
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
