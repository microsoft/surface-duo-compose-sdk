/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.composetesting

import androidx.compose.ui.geometry.Offset

data class ZoomCoordinates(
    val leftOuter: Offset,
    val leftInner: Offset,
    val rightInner: Offset,
    val rightOuter: Offset
) {
    override fun toString(): String {
        return "[LeftOuter: $leftOuter, LeftInner: $leftInner, RightInner: $rightInner, RightOuter: $rightOuter]"
    }
}
