/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.windowstate

import android.content.res.Configuration
import android.graphics.Rect
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

/**
 * Data class that contains foldable and large screen information extracted from the Jetpack
 * Window Manager library
 *
 * @param hasFold: true if window contains a FoldingFeature,
 * @param isFoldHorizontal: true if window contains a FoldingFeature with a horizontal orientation
 * @param foldBounds: Rect object that describes the bound of the FoldingFeature
 * @param foldState: state of the fold, based on state property of FoldingFeature
 * @param foldSeparates: based on isSeparating property of FoldingFeature
 * @param foldOccludes: true if FoldingFeature occlusion type is full
 * @param widthSizeClass: size class (compact, medium, or expanded) for window width
 * @param heightSizeClass: size class (compact, medium, or expanded) for window height
 */
data class WindowState(
    val hasFold: Boolean = false,
    val isFoldHorizontal: Boolean = false,
    val foldBounds: Rect = Rect(),
    val foldState: FoldState = FoldState.FLAT,
    val foldSeparates: Boolean = false,
    val foldOccludes: Boolean = false,
    val widthSizeClass: WindowSizeClass = WindowSizeClass.COMPACT,
    val heightSizeClass: WindowSizeClass = WindowSizeClass.MEDIUM,
) {
    private val foldableFoldSize = when (isFoldHorizontal) {
        true -> foldBounds.height()
        false -> foldBounds.width()
    }

    val foldablePaneWidth = when (isFoldHorizontal) {
        true -> foldBounds.right
        false -> foldBounds.left
    }

    val foldSize = if (hasFold) foldableFoldSize else 0

    val windowMode: WindowMode
        @Composable get() {
            // REVISIT: should width/height ratio be used instead of orientation?
            val isPortrait =
                LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

            return calculateWindowMode(isPortrait)
        }

    @VisibleForTesting
    fun calculateWindowMode(isPortrait: Boolean): WindowMode {
        // REVISIT: should height class also be considered?
        // Also, right now we are considering large screens + foldables mutually exclusive
        // (which seems necessary for dualscreen apps), but we may want to think about this
        // more and change our approach if we think there are cases where we want an app to
        // know about both properties
        val isLargeScreen = !hasFold && widthSizeClass == WindowSizeClass.EXPANDED

        return when {
            hasFold -> {
                if (isFoldHorizontal)
                    WindowMode.DUAL_LANDSCAPE
                else
                    WindowMode.DUAL_PORTRAIT
            }
            isLargeScreen -> {
                if (isPortrait)
                    WindowMode.DUAL_LANDSCAPE
                else
                    WindowMode.DUAL_PORTRAIT
            }
            isPortrait -> WindowMode.SINGLE_PORTRAIT
            else -> WindowMode.SINGLE_LANDSCAPE
        }
    }

    @Composable
    fun isDualScreen(): Boolean {
        return windowMode.isDualScreen
    }

    @Composable
    fun isDualPortrait(): Boolean {
        return windowMode == WindowMode.DUAL_PORTRAIT
    }

    @Composable
    fun isDualLandscape(): Boolean {
        return windowMode == WindowMode.DUAL_LANDSCAPE
    }

    @Composable
    fun isSinglePortrait(): Boolean {
        return windowMode == WindowMode.SINGLE_PORTRAIT
    }

    @Composable
    fun isSingleLandscape(): Boolean {
        return windowMode == WindowMode.SINGLE_LANDSCAPE
    }
}
