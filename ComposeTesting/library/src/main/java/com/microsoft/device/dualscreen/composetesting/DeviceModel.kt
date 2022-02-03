/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.composetesting

import android.util.Log
import android.view.Surface
import androidx.test.uiautomator.UiDevice

/**
 * DEVICE MODEL
 * -----------------------------------------------------------------------------------------------
 * The DeviceModel class and related helper functions can be used in dualscreen UI tests to help
 * calculate coordinates for simulated swipe gestures. Device properties are determined using
 * UiDevice.
 */

/**
 * Enum class that can be used to represent various device models and extract coordinates that can be used for
 * simulating gestures in UI tests
 *
 * For Microsoft Surface Duo devices, the coordinates are all from the dual portrait point of view, and dimensions
 * were taken from here: https://docs.microsoft.com /dual-screen/android/surface-duo-dimensions
 *
 * @param paneWidth: width of device panes in pixels in dual portrait mode (assumed to have panes of equal size)
 * @param paneHeight: height of device panes in pixels in dual portrait mode (assumed to have panes of equal size)
 * @param foldWidth: width of device fold in pixels in dual portrait mode
 * @param leftX: x-coordinate of the center of the left pane in dual portrait mode
 * @param rightX: x-coordinate of the center of the right pane in dual portrait mode
 * @param middleX: x-coordinate of the center of the device in dual portrait mode
 * @param middleY: y-coordinate of the center of the device in dual portrait mode
 * @param bottomY: y-coordinate of the bottom of the device in dual portrait mode
 * @param spanSteps: number of move steps to take when executing a span gesture, where one step takes ~ 5ms
 * @param unspanSteps: number of move steps to take when executing a unspan gesture, where one step takes ~ 5ms
 * @param switchSteps: number of move steps to take when executing a switch gesture, where one step takes ~ 5ms
 * @param closeSteps: number of move steps to take when executing a close gesture, where one step takes ~ 5ms
 */
enum class DeviceModel(
    val paneWidth: Int,
    val paneHeight: Int,
    val foldWidth: Int,
    val leftX: Int = paneWidth / 2,
    val rightX: Int = leftX + paneWidth + foldWidth,
    val middleX: Int = paneWidth + foldWidth / 2,
    val middleY: Int = paneHeight / 2,
    val bottomY: Int,
    val spanSteps: Int = 400,
    val unspanSteps: Int = 200,
    val switchSteps: Int = 100,
    val closeSteps: Int = 50,
) {
    SurfaceDuo(paneWidth = 1350, paneHeight = 1800, foldWidth = 84, bottomY = 1780),
    SurfaceDuo2(paneWidth = 1344, paneHeight = 1892, foldWidth = 66, bottomY = 1870),
    Other(paneWidth = 0, paneHeight = 0, foldWidth = 0, bottomY = 0);

    override fun toString(): String {
        return "$name [leftX: $leftX rightX: $rightX middleX: $middleX middleY: $middleY bottomY: $bottomY]"
    }
}

/**
 * Checks whether a device is a Surface Duo model
 */
fun UiDevice.isSurfaceDuo(): Boolean {
    val model = getDeviceModel()
    return model == DeviceModel.SurfaceDuo || model == DeviceModel.SurfaceDuo2
}

/**
 * Returns the hinge/fold size of a device in pixels
 */
fun UiDevice.getFoldSize(): Int {
    return getDeviceModel().foldWidth
}

/**
 * Determines the model of a device based on display width and height (in pixels)
 */
fun UiDevice.getDeviceModel(): DeviceModel {
    Log.d(
        "DeviceModel",
        "w: $displayWidth h: $displayHeight rotation: $displayRotation"
    )

    return when (displayRotation) {
        Surface.ROTATION_0, Surface.ROTATION_180 -> getModelFromPaneWidth(displayWidth)
        Surface.ROTATION_90, Surface.ROTATION_270 -> getModelFromPaneWidth(displayHeight)
        else -> throw Error("Unknown rotation state $displayRotation")
    }
}

/**
 * Helper method to compare the pane width of a device to the pane widths of the defined device
 * models
 *
 * @param paneWidth: pane width in pixels
 */
private fun UiDevice.getModelFromPaneWidth(paneWidth: Int): DeviceModel {
    for (model in DeviceModel.values()) {
        // pane width could be the width of a single pane, or the width of two panes + the width
        // of the hinge
        if (paneWidth == model.paneWidth || paneWidth == model.paneWidth * 2 + model.foldWidth)
            return model
    }
    Log.d(
        "DeviceModel",
        "Unknown dualscreen device dimensions $displayWidth $displayHeight"
    )
    return DeviceModel.Other
}
