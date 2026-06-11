package com.messfit.ai.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MessFitColorScheme = darkColorScheme(
    primary = NeonGreen,
    onPrimary = Black,
    secondary = NeonGreenDim,
    onSecondary = Black,
    background = Black,
    onBackground = TextPrimary,
    surface = DarkGrey,
    onSurface = TextPrimary,
    surfaceVariant = MediumGrey,
    onSurfaceVariant = TextSecondary,
    outline = GlassBorder
)

@Composable
fun MessFitTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MessFitColorScheme,
        typography = MessFitTypography,
        content = content
    )
}
