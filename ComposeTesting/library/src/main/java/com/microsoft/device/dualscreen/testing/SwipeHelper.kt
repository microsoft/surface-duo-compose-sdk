/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.testing

import android.view.Surface
import androidx.test.uiautomator.UiDevice

/**
 * SWIPE HELPER
 * -----------------------------------------------------------------------------------------------
 * These functions can be used in dualscreen UI tests to simulate swipe gestures that affect
 * app display. The swipes are simulated using UiDevice, and the coordinates are calculated based
 * on the display width/height of the testing device (see DeviceModel.kt).
 *
 * Available gestures:
 * - span (display app in two panes)
 * - unspan (display app in one pane)
 * - close (close app)
 * - switch (switch app from one pane to the other)
 */

/**
 * Helper method that sets up/cleans up a dualscreen swipe operation for automated testing
 * (freezes rotation, retrieves device model, performs swipe, unfreezes rotation)
 */
private fun UiDevice.dualscreenSwipeWrapper(swipe: (DeviceModel) -> Boolean) {
    freezeRotation()

    val model = getDeviceModel()
    swipe(model)

    unfreezeRotation()
}

/**
 * Span app from the top/left pane
 */
fun UiDevice.spanFromStart() {
    dualscreenSwipeWrapper { model ->
        when (displayRotation) {
            Surface.ROTATION_0, Surface.ROTATION_180 ->
                swipe(model.leftX, model.bottomY, model.middleX, model.middleY, model.spanSteps)
            Surface.ROTATION_270 ->
                swipe(model.bottomY, model.leftX, model.middleY, model.middleX, model.spanSteps)
            Surface.ROTATION_90 ->
                swipe(model.bottomY, model.leftX, model.middleY, model.middleX, model.spanSteps)
            else -> throw Error("Unknown rotation state $displayRotation")
        }
    }
}

/**
 * Span app from the bottom/right pane
 */
fun UiDevice.spanFromEnd() {
    dualscreenSwipeWrapper { model ->
        when (displayRotation) {
            Surface.ROTATION_0, Surface.ROTATION_180 ->
                swipe(model.rightX, model.bottomY, model.middleX, model.middleY, model.spanSteps)
            Surface.ROTATION_270 ->
                swipe(model.bottomY, model.rightX, model.middleY, model.middleX, model.spanSteps)
            Surface.ROTATION_90 ->
                swipe(model.bottomY, model.rightX, model.middleY, model.middleX, model.spanSteps)
            else -> throw Error("Unknown rotation state $displayRotation")
        }
    }
}

/**
 * Unspan app to the top/left pane
 */
fun UiDevice.unspanToStart() {
    dualscreenSwipeWrapper { model ->
        when (displayRotation) {
            Surface.ROTATION_0, Surface.ROTATION_180 ->
                swipe(model.rightX, model.bottomY, model.leftX, model.middleY, model.unspanSteps)
            Surface.ROTATION_270 ->
                swipe(model.bottomY, model.rightX, model.middleY, model.leftX, model.unspanSteps)
            Surface.ROTATION_90 ->
                swipe(model.bottomY, model.rightX, model.middleY, model.leftX, model.unspanSteps)
            else -> throw Error("Unknown rotation state $displayRotation")
        }
    }
}

/**
 * Unspan app to bottom/right pane
 */
fun UiDevice.unspanToEnd() {
    dualscreenSwipeWrapper { model ->
        when (displayRotation) {
            Surface.ROTATION_0, Surface.ROTATION_180 ->
                swipe(model.leftX, model.bottomY, model.rightX, model.middleY, model.unspanSteps)
            Surface.ROTATION_270 ->
                swipe(model.bottomY, model.leftX, model.middleY, model.rightX, model.unspanSteps)
            Surface.ROTATION_90 ->
                swipe(model.bottomY, model.leftX, model.middleY, model.rightX, model.unspanSteps)
            else -> throw Error("Unknown rotation state $displayRotation")
        }
    }
}

/**
 * Switch app from bottom/right pane to top/left pane
 */
fun UiDevice.switchToStart() {
    dualscreenSwipeWrapper { model ->
        when (displayRotation) {
            Surface.ROTATION_0, Surface.ROTATION_180 ->
                swipe(model.rightX, model.bottomY, model.leftX, model.middleY, model.switchSteps)
            Surface.ROTATION_270 ->
                swipe(model.bottomY, model.rightX, model.middleY, model.leftX, model.switchSteps)
            Surface.ROTATION_90 ->
                swipe(model.bottomY, model.rightX, model.middleY, model.leftX, model.switchSteps)
            else -> throw Error("Unknown rotation state $displayRotation")
        }
    }
}

/**
 * Switch app from top/left pane to bottom/right pane
 */
fun UiDevice.switchToEnd() {
    dualscreenSwipeWrapper { model ->
        when (displayRotation) {
            Surface.ROTATION_0, Surface.ROTATION_180 ->
                swipe(model.leftX, model.bottomY, model.rightX, model.middleY, model.switchSteps)
            Surface.ROTATION_270 ->
                swipe(model.bottomY, model.leftX, model.middleY, model.rightX, model.switchSteps)
            Surface.ROTATION_90 ->
                swipe(model.bottomY, model.leftX, model.middleY, model.rightX, model.switchSteps)
            else -> throw Error("Unknown rotation state $displayRotation")
        }
    }
}

/**
 * Close app from top/left pane
 */
fun UiDevice.closeStart() {
    dualscreenSwipeWrapper { model ->
        when (displayRotation) {
            Surface.ROTATION_0, Surface.ROTATION_180 ->
                swipe(model.leftX, model.bottomY, model.leftX, model.middleY, model.closeSteps)
            Surface.ROTATION_270 ->
                swipe(model.bottomY, model.leftX, model.middleY, model.leftX, model.closeSteps)
            Surface.ROTATION_90 ->
                swipe(model.bottomY, model.leftX, model.middleY, model.leftX, model.closeSteps)
            else -> throw Error("Unknown rotation state $displayRotation")
        }
    }
}

/**
 * Close app from bottom/right pane
 */
fun UiDevice.closeEnd() {
    dualscreenSwipeWrapper { model ->
        when (displayRotation) {
            Surface.ROTATION_0, Surface.ROTATION_180 ->
                swipe(model.rightX, model.bottomY, model.rightX, model.middleY, model.closeSteps)
            Surface.ROTATION_270 ->
                swipe(model.bottomY, model.rightX, model.middleY, model.rightX, model.closeSteps)
            Surface.ROTATION_90 ->
                swipe(model.bottomY, model.rightX, model.middleY, model.rightX, model.closeSteps)
            else -> throw Error("Unknown rotation state $displayRotation")
        }
    }
}
