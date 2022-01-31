/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.composetesting

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.TouchInjectionScope
import androidx.compose.ui.test.pinch

/**
 * ZOOM HELPER
 * -----------------------------------------------------------------------------------------------
 * These functions can be used to perform zooming gestures during Compose UI tests.
 */

const val PINCH_MILLIS: Long = 500

/**
 * Performs a zoom in gesture (swipes start towards center then move outwards)
 *
 * @param pinchMillis: number of milliseconds it takes to perform the pinch (default 500)
 */
fun TouchInjectionScope.zoomIn(pinchMillis: Long = PINCH_MILLIS) {
    val coords = setupZoomCoords()
    Log.d("ZoomHelper", "Zooming in: $coords")
    pinch(coords.leftInner, coords.leftOuter, coords.rightInner, coords.rightOuter, pinchMillis)
}

/**
 * Performs a zoom out gesture (swipes start towards center then move outwards)
 *
 * @param pinchMillis: number of milliseconds it takes to perform the pinch (default 500)
 */
fun TouchInjectionScope.zoomOut(pinchMillis: Long = PINCH_MILLIS) {
    val coords = setupZoomCoords()
    Log.d("ZoomHelper", "Zooming out: $coords")
    pinch(coords.leftOuter, coords.leftInner, coords.rightOuter, coords.rightInner, pinchMillis)
}

/**
 * Calculates starting and ending zoom coordinates based on GestureScope properties
 */
private fun TouchInjectionScope.setupZoomCoords(): ZoomCoordinates {
    // Get height and width of node
    val width = (right - left).toLong()
    val height = (bottom - top).toLong()

    // Set up zoom coordinates offsets
    return ZoomCoordinates(
        leftOuter = Offset(left + width * 0.25f, top + height * 0.3f),
        leftInner = Offset(left + width * 0.45f, top + height * 0.3f),
        rightInner = Offset(left + width * 0.55f, height * 0.7f),
        rightOuter = Offset(left + width * 0.75f, height * 0.7f),
    )
}
