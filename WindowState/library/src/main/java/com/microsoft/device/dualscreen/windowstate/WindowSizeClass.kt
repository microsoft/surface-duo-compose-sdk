/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.windowstate

import androidx.annotation.VisibleForTesting
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class WindowSizeClass { COMPACT, MEDIUM, EXPANDED }

/**
 * Calculates size class for a given dimension
 *
 * @param dimenDp: size of dimension in Dp
 * @param dimen: which dimension is being measured (width or height)
 */
@VisibleForTesting
fun getWindowSizeClass(dimenDp: Dp, dimen: Dimension = Dimension.WIDTH): WindowSizeClass =
    when (dimen) {
        Dimension.WIDTH -> getSizeClass(dimenDp, 600.dp, 840.dp)
        Dimension.HEIGHT -> getSizeClass(dimenDp, 480.dp, 900.dp)
    }

private fun getSizeClass(size: Dp, medium: Dp, expanded: Dp): WindowSizeClass = when {
    size < 0.dp -> throw IllegalArgumentException("Dp value cannot be negative")
    size < medium -> WindowSizeClass.COMPACT
    size < expanded -> WindowSizeClass.MEDIUM
    else -> WindowSizeClass.EXPANDED
}
