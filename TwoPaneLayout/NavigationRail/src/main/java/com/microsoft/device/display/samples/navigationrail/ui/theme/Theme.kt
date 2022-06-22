/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.navigationrail.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = OxfordBlue,
    primaryVariant = TurquoiseTranslucent,
    onPrimary = Turquoise,
    secondary = Turquoise,
    secondaryVariant = MiddleBlueGreenTranslucent,
    surface = SpaceCadet,
    onSurface = Turquoise,
    background = SpaceCadet,
    onBackground = Turquoise,
    error = Turquoise,
)

private val LightColorPalette = lightColors(
    primary = Ming,
    primaryVariant = MingTranslucent,
    onPrimary = OffWhite,
    secondary = MiddleBlueGreen,
    secondaryVariant = MiddleBlueGreenTranslucent,
    surface = AliceBlue,
    onSurface = Ming,
    background = AliceBlue,
    onBackground = Ming,
    error = LightModeSelector,
)

@Composable
fun NavigationRailAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
