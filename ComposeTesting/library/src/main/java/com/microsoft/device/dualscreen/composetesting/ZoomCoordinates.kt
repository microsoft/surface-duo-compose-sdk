/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.composetesting

import androidx.compose.ui.geometry.Offset

/**
 * Stores pinch start and end coordinates in order to simulate a zooming gesture
 *
 * @param leftOuter: outer value (closer to edge of screen/zero) for the left finger
 * @param leftInner: inner value (closer to center of screen/max width) for the left finger
 * @param rightOuter: outer value (closer to edge of screen/max width) for the right finger
 * @param rightInner: inner value (closer to center of screen/zero) for the right finger
 */
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
