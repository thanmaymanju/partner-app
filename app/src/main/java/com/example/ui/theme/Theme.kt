package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val WhiteOrangeColorScheme =
  lightColorScheme(
    primary = PrimaryOrange,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = LightOrange,
    onPrimaryContainer = Color(0xFF622C00),
    secondary = SecondaryText,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = SubtleSurface,
    onSecondaryContainer = PrimaryText,
    tertiary = SuccessGreen,
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFE5F4EC),
    onTertiaryContainer = Color(0xFF14341F),
    background = MainBackground,
    onBackground = PrimaryText,
    surface = CardBackground,
    onSurface = PrimaryText,
    surfaceVariant = SubtleSurface,
    onSurfaceVariant = SecondaryText,
    outline = BorderColor,
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF)
  )

@Composable
fun PartnerTheme(
  darkTheme: Boolean = false,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = WhiteOrangeColorScheme,
    typography = Typography,
    content = content
  )
}
