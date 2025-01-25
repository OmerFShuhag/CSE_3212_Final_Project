package com.example.traction.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Define your custom colors
//private val NavyBlue = Color(0xFF264653)
//private val Teal = Color(0xFF2A9D8F)
//private val LightGray = Color(0xFFE9ECEF)
//private val White = Color(0xFFFFFFFF)
//private val SoftYellow = Color(0xFFE9C46A)
//private val Coral = Color(0xFFF4A261)
//private val MutedRed = Color(0xFFE76F51)
//private val CharcoalGray = Color(0xFF4A4A4A)

// Define the dark color scheme
private val DarkColorScheme = darkColorScheme(
    primary = NavyBlue,
    secondary = Teal,
    tertiary = SoftYellow,
    background = Color(0xFF121212), // Dark mode background
    surface = Color(0xFF1E1E1E),   // Dark mode surface
    onPrimary = White,
    onSecondary = LightGray,
    onTertiary = CharcoalGray,
    onBackground = LightGray,
    onSurface = LightGray,
    error = MutedRed
)

// Define the light color scheme
private val LightColorScheme = lightColorScheme(
    primary = NavyBlue,
    secondary = Teal,
    tertiary = SoftYellow,
    background = LightGray,
    surface = White,
    onPrimary = White,
    onSecondary = CharcoalGray,
    onTertiary = CharcoalGray,
    onBackground = CharcoalGray,
    onSurface = CharcoalGray,
    error = MutedRed
)

@Composable
fun TracTionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true, // Optionally use dynamic colors on Android 12+
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Use your custom Typography if defined
        content = content
    )
}
