/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.testutils

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
 * Coordinates taken from dual portrait point of view
 * Dimensions available here: https://docs.microsoft.com /dual-screen/android/surface-duo-dimensions
 */
enum class DeviceModel(
    val paneWidth: Int,
    val paneHeight: Int,
    val foldSize: Int,
    val leftX: Int = paneWidth / 2,
    val rightX: Int = leftX + paneWidth + foldSize,
    val middleX: Int = paneWidth + foldSize / 2,
    val middleY: Int = paneHeight / 2,
    val bottomY: Int,
    val spanSteps: Int = 400,
    val unspanSteps: Int = 200,
    val switchSteps: Int = 100,
    val closeSteps: Int = 50,
) {
    SurfaceDuo(paneWidth = 1350, paneHeight = 1800, foldSize = 84, bottomY = 1780),
    SurfaceDuo2(paneWidth = 1344, paneHeight = 1892, foldSize = 66, bottomY = 1870),
    Other(paneWidth = 0, paneHeight = 0, foldSize = 0, bottomY = 0);

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
 * Returns the hinge/fold size of a device
 */
fun UiDevice.getFoldSize(): Int {
    return getDeviceModel().foldSize
}

/**
 * Determines the model of a device based on display width and height
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
 */
private fun UiDevice.getModelFromPaneWidth(paneWidth: Int): DeviceModel {
    for (model in DeviceModel.values()) {
        // pane width could be the width of a single pane, or the width of two panes + the width
        // of the hinge
        if (paneWidth == model.paneWidth || paneWidth == model.paneWidth * 2 + model.foldSize)
            return model
    }
    Log.d(
        "DeviceModel",
        "Unknown dualscreen device dimensions $displayWidth $displayHeight"
    )
    return DeviceModel.Other
}
