package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Semantic color tokens for YouTube Studio
data class StudioColorTokens(
    val background: Color,
    val surface: Color,
    val cardBackground: Color,
    val cardBorder: Color,
    val divider: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val chipBackground: Color,
    val chipSelectedBackground: Color,
    val chipSelectedText: Color,
    val chipBorder: Color,
    val searchBackground: Color,
    val navBackground: Color,
    val navSelected: Color,
    val navUnselected: Color,
    val accentRed: Color = StudioRed,
    val accentGreen: Color = StudioGreen,
    val accentTeal: Color = StudioCyan,
    val accentPurple: Color = StudioPurple,
    val isDark: Boolean = true
)

val DarkStudioColors = StudioColorTokens(
    background = StudioPureBlack,
    surface = StudioPureBlack,
    cardBackground = StudioCardDark,
    cardBorder = StudioCardBorderDark,
    divider = StudioDividerDark,
    textPrimary = StudioTextWhite,
    textSecondary = StudioTextMuted,
    textTertiary = Color(0xFF636366),
    chipBackground = Color(0xFF212121),
    chipSelectedBackground = Color(0xFFFFFFFF),
    chipSelectedText = Color(0xFF000000),
    chipBorder = Color(0xFF333333),
    searchBackground = Color(0xFF212121),
    navBackground = StudioPureBlack,
    navSelected = StudioNavSelectedRed,
    navUnselected = StudioTextMuted,
    isDark = true
)

val LightStudioColors = StudioColorTokens(
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    cardBackground = Color(0xFFFFFFFF),
    cardBorder = StudioCardBorder,
    divider = StudioDivider,
    textPrimary = StudioBlack,
    textSecondary = StudioSecondaryText,
    textTertiary = StudioTertiaryText,
    chipBackground = Color(0xFFF2F2F2),
    chipSelectedBackground = StudioBlack,
    chipSelectedText = Color(0xFFFFFFFF),
    chipBorder = StudioCardBorder,
    searchBackground = Color(0xFFF2F2F2),
    navBackground = Color(0xFFFFFFFF),
    navSelected = StudioBlack,
    navUnselected = StudioSecondaryText,
    isDark = false
)

val LocalStudioColors = staticCompositionLocalOf { DarkStudioColors }

object StudioTheme {
    val colors: StudioColorTokens
        @Composable
        @ReadOnlyComposable
        get() = LocalStudioColors.current
}

private val DarkM3ColorScheme = darkColorScheme(
    primary = StudioTextWhite,
    onPrimary = StudioPureBlack,
    primaryContainer = StudioCardDark,
    onPrimaryContainer = StudioTextWhite,
    secondary = StudioTextMuted,
    onSecondary = StudioPureBlack,
    background = StudioPureBlack,
    onBackground = StudioTextWhite,
    surface = StudioPureBlack,
    onSurface = StudioTextWhite,
    surfaceVariant = StudioCardDark,
    onSurfaceVariant = StudioTextMuted,
    outline = StudioCardBorderDark,
    outlineVariant = StudioDividerDark
)

private val LightM3ColorScheme = lightColorScheme(
    primary = StudioBlack,
    onPrimary = Color.White,
    primaryContainer = StudioLightGray,
    onPrimaryContainer = StudioBlack,
    secondary = StudioSecondaryText,
    onSecondary = Color.White,
    background = StudioBackground,
    onBackground = StudioDarkText,
    surface = StudioSurface,
    onSurface = StudioDarkText,
    surfaceVariant = StudioLightGray,
    onSurfaceVariant = StudioSecondaryText,
    outline = StudioCardBorder,
    outlineVariant = StudioDivider
)

@Composable
fun MyApplicationTheme(
    // Default to true (Dark Mode) matching reference YouTube Studio screenshot & recording
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val studioColors = if (darkTheme) DarkStudioColors else LightStudioColors
    val m3ColorScheme = if (darkTheme) DarkM3ColorScheme else LightM3ColorScheme

    CompositionLocalProvider(LocalStudioColors provides studioColors) {
        MaterialTheme(
            colorScheme = m3ColorScheme,
            typography = Typography,
            content = content
        )
    }
}
