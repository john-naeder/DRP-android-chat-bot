package com.mdev.chatapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val md_theme_light_primary = Color(0xFF00BFA5)
val md_theme_light_onPrimary = Color(0xFF000000)
val md_theme_light_primaryContainer = Color(0xFFFFFFFF)
val md_theme_light_onPrimaryContainer = Color(0xFF000000)
val md_theme_light_secondary = Color(0xFF000000)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFFFFFFF)
val md_theme_light_onSecondaryContainer = Color(0xFF000000)
val md_theme_light_tertiary = Color(0xFF000000)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFFFFFFF)
val md_theme_light_onTertiaryContainer = Color(0xFF000000)
val md_theme_light_error = Color(0xFFB00020)
val md_theme_light_errorContainer = Color(0xFFFFFFFF)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_onErrorContainer = Color(0xFFB00020)
val md_theme_light_background = Color(0xFFFFFFFF)
val md_theme_light_onBackground = Color(0xFF000000)
val md_theme_light_surface = Color(0xFFF5F5F5)
val md_theme_light_onSurface = Color(0xFF000000)
val md_theme_light_surfaceVariant = Color(0xFFE0E0E0)
val md_theme_light_onSurfaceVariant = Color(0xFF000000)
val md_theme_light_outline = Color(0xFF000000)
val md_theme_light_inverseOnSurface = Color(0xFFFFFFFF)
val md_theme_light_inverseSurface = Color(0xFF000000)
val md_theme_light_inversePrimary = Color(0xFF00BFA5)
val md_theme_light_shadow = Color(0xFF000000)
val md_theme_light_surfaceTint = Color(0x1FFFFFFF)
val md_theme_light_outlineVariant = Color(0xFF000000)
val md_theme_light_scrim = Color(0x99000000)
val md_theme_light_focusedTextFieldText = Color(0xFF000000)
val md_theme_light_unfocusedTextFieldText = Color(0xFF475569)
val md_theme_light_textFieldContainer = Color(0xFFE0E0E0)

val md_theme_dark_primary = Color(0xFF00BFA5)
val md_theme_dark_onPrimary = Color(0xFF000000)
val md_theme_dark_primaryContainer = Color(0xFFFFFFFF)
val md_theme_dark_onPrimaryContainer = Color(0xFF000000)
val md_theme_dark_secondary = Color(0xFF000000)
val md_theme_dark_onSecondary = Color(0xFFFFFFFF)
val md_theme_dark_secondaryContainer = Color(0xFFFFFFFF)
val md_theme_dark_onSecondaryContainer = Color(0xFF000000)
val md_theme_dark_tertiary = Color(0xFF000000)
val md_theme_dark_onTertiary = Color(0xFFFFFFFF)
val md_theme_dark_tertiaryContainer = Color(0xFFFFFFFF)
val md_theme_dark_onTertiaryContainer = Color(0xFF000000)
val md_theme_dark_error = Color(0xFFB00020)
val md_theme_dark_errorContainer = Color(0xFFFFFFFF)
val md_theme_dark_onError = Color(0xFFFFFFFF)
val md_theme_dark_onErrorContainer = Color(0xFFB00020)
val md_theme_dark_background = Color(0xFF121212)
val md_theme_dark_onBackground = Color(0xFFFFFFFF)
val md_theme_dark_surface = Color(0xFF1E1E1E)
val md_theme_dark_onSurface = Color(0xFFFFFFFF)
val md_theme_dark_surfaceVariant = Color(0xFF2E2E2E)
val md_theme_dark_onSurfaceVariant = Color(0xFFFFFFFF)
val md_theme_dark_outline = Color(0xFFFFFFFF)
val md_theme_dark_inverseOnSurface = Color(0xFF000000)
val md_theme_dark_inverseSurface = Color(0xFFFFFFFF)
val md_theme_dark_inversePrimary = Color(0xFF00BFA5)
val md_theme_dark_shadow = Color(0xFF000000)
val md_theme_dark_surfaceTint = Color(0x1FFFFFFF)
val md_theme_dark_outlineVariant = Color(0xFFFFFFFF)
val md_theme_dark_scrim = Color(0x99000000)
val md_theme_dark_focusedTextFieldText = Color(0xFFFFFFFF)
val md_theme_dark_unfocusedTextFieldText = Color(0xFF475569)
val md_theme_dark_textFieldContainer = Color(0xFF2E2E2E)

val seed = Color(0xFF175A00)
val ColorScheme.focusedTextFieldText
    @Composable
    get() = if (isSystemInDarkTheme()) Color.White else Color.Black

val ColorScheme.unfocusedTextFieldText
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF94A3B8) else Color(0xFF475569)

val ColorScheme.textFieldContainer
    @Composable
    get() = if (isSystemInDarkTheme()) md_theme_dark_surfaceVariant else md_theme_light_tertiaryContainer
