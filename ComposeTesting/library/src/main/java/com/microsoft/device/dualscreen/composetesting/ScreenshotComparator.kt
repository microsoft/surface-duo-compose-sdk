/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.composetesting

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.captureToImage
import androidx.core.graphics.toColor
import androidx.test.platform.app.InstrumentationRegistry
import java.io.FileOutputStream
import kotlin.math.abs

/**
 * SCREENSHOT COMPARATOR
 * -----------------------------------------------------------------------------------------------
 * These functions can be used to take, save, and compare screenshots of composables in UI tests.
 *
 * Based on ScreenshotComparator.kt in the TestingCodelab project from the official Jetpack Compose codelab samples
 * https://github.com/googlecodelabs/android-compose-codelabs/blob/main/TestingCodelab/app/src/androidTest/java/com/example/compose/rally/ScreenshotComparator.kt
 */

/**
 * Check whether a screenshot of the current node matches the reference image
 *
 * @param referenceAsset: name of reference image (must be stored in the androidTest/assets folder)
 * @param node: Semantics Node to take screenshot of
 */
@RequiresApi(Build.VERSION_CODES.O)
fun assertScreenshotMatchesReference(
    referenceAsset: String,
    node: SemanticsNodeInteraction
) {
    // Capture screenshot of composable
    val bitmap = node.captureToImage().asAndroidBitmap()

    // Load reference screenshot from instrumentation test assets
    val referenceBitmap = InstrumentationRegistry.getInstrumentation().context.resources.assets.open(referenceAsset)
        .use { BitmapFactory.decodeStream(it) }

    // Compare bitmaps
    assert(referenceBitmap.compare(bitmap))
}

/**
 * Saves a screenshot of the current node to the device's internal storage - screenshots can be
 * retrieved via adb as described in these instructions:
 * https://stackoverflow.com/questions/40323126/where-do-i-find-the-saved-image-in-android
 *
 * @param filename: filename (including extension) of the screenshot
 * @param node: Semantics Node to take screenshot of
 */
@RequiresApi(Build.VERSION_CODES.O)
fun saveScreenshotToDevice(filename: String, node: SemanticsNodeInteraction) {
    // Capture screenshot of composable
    val bmp = node.captureToImage().asAndroidBitmap()

    // Get path for saving file
    val path = InstrumentationRegistry.getInstrumentation().targetContext.filesDir.canonicalPath

    // Compress bitmap and send to file
    FileOutputStream("$path/$filename").use { out ->
        bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
    }

    Log.d("Screenshot Comparator", "Saved screenshot to $path/$filename")
}

/**
 * Checks whether two bitmaps are the same, allowing for a small percentage of different pixels
 * due to differences between devices
 *
 * @param other: Bitmap to compare to
 * @return true if at least 99% of the bitmap pixels match, false otherwise
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Bitmap.compare(other: Bitmap): Boolean {
    if (this.width != other.width || this.height != other.height) {
        return false
    }
    // Compare row by row to save memory on device
    val row1 = IntArray(width)
    val row2 = IntArray(width)
    var numDiffs = 0

    for (column in 0 until height) {
        // Read one row per bitmap and compare
        this.getRow(row1, column)
        other.getRow(row2, column)
        row1.forEachIndexed { index, element ->
            if (!row2[index].isSimilarColor(element)) {
                numDiffs++
            }
        }
    }

    // Throw error if greater than 1% of the bitmap's pixels are different
    if (numDiffs > 0.01 * width * width) {
        Log.d("Screen Comparator", "Sizes match but bitmap content has differences in $numDiffs pixels")
        return false
    }
    Log.d("Screen Comparator", "Number of different pixels: $numDiffs")
    return true
}

/**
 * Checks whether two colors (represented by integer values) are similar
 *
 * @param other: Color integer to compare to
 * @return true if all color components are within 0.5% of the original, false otherwise
 */
@RequiresApi(Build.VERSION_CODES.O)
private fun Int.isSimilarColor(other: Int): Boolean {
    // Convert ints to color values
    val expectedColor = this.toColor()
    val actualColor = other.toColor()

    // Compare individual color components
    val expectedComponents = expectedColor.components
    val actualComponents = actualColor.components

    // Check that color components have equal sizes
    if (expectedComponents.size != actualComponents.size) {
        return false
    }

    // Check that each color component is similar (within +/- 0.5%)
    val percentError = 0.005
    expectedComponents.forEachIndexed { index, comp ->
        // Calculate the error allowance for the color component
        val maxColorVal = expectedColor.colorSpace.getMaxValue(index)
        val minColorVal = expectedColor.colorSpace.getMinValue(index)
        val errorAllowance = percentError * (maxColorVal - minColorVal)

        // Compare color component values
        if (abs(actualComponents[index] - comp) > errorAllowance) {
            return false
        }
    }
    return true
}

private fun Bitmap.getRow(pixels: IntArray, column: Int) {
    this.getPixels(pixels, 0, width, 0, column, width, 1)
}
