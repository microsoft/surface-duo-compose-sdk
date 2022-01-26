/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.windowstate

import android.content.res.Configuration
import android.graphics.RectF
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 * Data class that contains foldable and large screen information extracted from the Jetpack
 * Window Manager library
 *
 * @param hasFold: true if window contains a FoldingFeature,
 * @param foldIsHorizontal: true if window contains a FoldingFeature with a horizontal orientation
 * @param foldBoundsDp: RectF object that describes the bound of the FoldingFeature in Dp
 * @param foldState: state of the fold, based on state property of FoldingFeature
 * @param foldIsSeparating: based on isSeparating property of FoldingFeature
 * @param foldIsOccluding: true if FoldingFeature occlusion type is full
 * @param windowWidthDp: Dp value of the window width
 * @param windowHeightDp: Dp value of the window height
 */
data class WindowState(
    val hasFold: Boolean = false,
    val foldIsHorizontal: Boolean = false,
    val foldBoundsDp: RectF = RectF(),
    val foldState: FoldState = FoldState.FLAT,
    val foldIsSeparating: Boolean = false,
    val foldIsOccluding: Boolean = false,
    val windowWidthDp: Dp = 0.dp,
    val windowHeightDp: Dp = 0.dp,
) {
    /**
     * Returns a dp value of the width of the hinge or the folding line if it is separating, otherwise 0
     */
    val foldSizeDp: Dp =
        if (foldIsSeparating) {
            if (foldIsHorizontal) foldBoundsDp.height().dp else foldBoundsDp.width().dp
        } else {
            0.dp
        }

    /**
     * Returns the current window mode (single portrait, single landscape, dual portrait, or dual landscape)
     */
    val windowMode: WindowMode
        @Composable get() {
            val isPortrait = windowIsPortrait()

            return calculateWindowMode(isPortrait)
        }

    /**
     * Returns whether the current window is in a dual screen mode or not
     *
     * A window is considered dual screen if it's a large screen ("expanded" width size class) or a foldable
     * (folding feature is present and is separating)
     */
    @Composable
    fun isDualScreen(): Boolean {
        return windowMode.isDualScreen
    }

    /**
     * Returns whether the current window is in dual portrait mode or not
     */
    @Composable
    fun isDualPortrait(): Boolean {
        return windowMode == WindowMode.DUAL_PORTRAIT
    }

    /**
     * Returns whether the current window is in dual landscape mode or not
     */
    @Composable
    fun isDualLandscape(): Boolean {
        return windowMode == WindowMode.DUAL_LANDSCAPE
    }

    /**
     * Returns whether the current window is in single portrait mode or not
     */
    @Composable
    fun isSinglePortrait(): Boolean {
        return windowMode == WindowMode.SINGLE_PORTRAIT
    }

    /**
     * Returns whether the current window is in single landscape mode or not
     */
    @Composable
    fun isSingleLandscape(): Boolean {
        return windowMode == WindowMode.SINGLE_LANDSCAPE
    }

    /**
     * Returns the size class (compact, medium, or expanded) for the window width
     */
    fun widthSizeClass(): WindowSizeClass {
        return getWindowSizeClass(windowWidthDp)
    }

    /**
     * Returns the size class (compact, medium, or expanded) for the window height
     */
    fun heightSizeClass(): WindowSizeClass {
        return getWindowSizeClass(windowHeightDp, Dimension.HEIGHT)
    }

    /**
     * Returns the dp size of pane 1 (top and left or right pane depending on local layout direction) when a fold
     * is separating, otherwise the returned size is zero
     */
    val foldablePane1SizeDp: Size
        @Composable get() {
            return getFoldablePaneSizes(LocalLayoutDirection.current).first
        }

    /**
     * Returns the dp size of pane 2 (bottom and left or right pane depending on local layout direction) when a
     * fold is separating, otherwise the returned size is zero
     */
    val foldablePane2SizeDp: Size
        @Composable get() {
            return getFoldablePaneSizes(LocalLayoutDirection.current).second
        }

    /**
     * Returns the dp sizes of pane 1 and pane 2 in a pair when a fold is separating, otherwise the returned sizes
     * are zero
     */
    val foldablePaneSizesDp: Pair<Size, Size>
        @Composable get() {
            return getFoldablePaneSizes(LocalLayoutDirection.current)
        }

    /**
     * Returns the dp size of pane 1 (top and left or right pane depending on local layout direction) when a fold
     * is separating or the window is large, otherwise the returned size is zero
     */
    @Composable
    fun pane1SizeDp(pane1Weight: Float = 0.5f): Size {
        return getPaneSizes(windowIsPortrait(), LocalLayoutDirection.current, pane1Weight).first
    }

    /**
     * Returns the dp size of pane 2 (bottom and left or right pane depending on local layout direction) when a
     * fold is separating or the window is large, otherwise the returned size is zero
     */
    @Composable
    fun pane2SizeDp(pane1Weight: Float = 0.5f): Size {
        return getPaneSizes(windowIsPortrait(), LocalLayoutDirection.current, pane1Weight).second
    }

    /**
     * Returns the dp sizes of pane 1 and pane 2 in a pair when a fold is separating or a window is large,
     * otherwise the returned sizes are zero
     */
    @Composable
    fun paneSizesDp(pane1Weight: Float = 0.5f): Pair<Size, Size> {
        return getPaneSizes(windowIsPortrait(), LocalLayoutDirection.current, pane1Weight)
    }

    @Composable
    private fun windowIsPortrait(): Boolean {
        // REVISIT: should width/height ratio of the window be used instead of orientation?
        return LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    @VisibleForTesting
    fun calculateWindowMode(isPortrait: Boolean): WindowMode {
        // REVISIT: should height class also be considered?
        // Also, right now we are considering large screens + foldables mutually exclusive
        // (which seems necessary for dualscreen apps), but we may want to think about this
        // more and change our approach if we think there are cases where we want an app to
        // know about both properties
        val widthSizeClass = getWindowSizeClass(windowWidthDp)
        val isLargeScreen = !foldIsSeparating && widthSizeClass == WindowSizeClass.EXPANDED

        return when {
            foldIsSeparating -> if (foldIsHorizontal) WindowMode.DUAL_LANDSCAPE else WindowMode.DUAL_PORTRAIT
            isLargeScreen -> if (isPortrait) WindowMode.DUAL_LANDSCAPE else WindowMode.DUAL_PORTRAIT
            else -> if (isPortrait) WindowMode.SINGLE_PORTRAIT else WindowMode.SINGLE_LANDSCAPE
        }
    }

    @VisibleForTesting
    fun getFoldablePaneSizes(layoutDir: LayoutDirection): Pair<Size, Size> {
        if (!foldIsSeparating)
            return Pair(Size(0f, 0f), Size(0f, 0f))

        if (foldIsHorizontal) {
            val paneWidth = windowWidthDp.value

            val topPaneHeight = foldBoundsDp.top.toFloat()
            val bottomPaneHeight = windowHeightDp.value - foldBoundsDp.bottom

            val topPaneSize = Size(paneWidth, topPaneHeight)
            val bottomPaneSize = Size(paneWidth, bottomPaneHeight)

            return Pair(topPaneSize, bottomPaneSize)
        } else {
            val paneHeight = windowHeightDp.value

            val leftPaneWidth = foldBoundsDp.left.toFloat()
            val rightPaneWidth = windowWidthDp.value - foldBoundsDp.right

            val leftPaneSize = Size(leftPaneWidth, paneHeight)
            val rightPaneSize = Size(rightPaneWidth, paneHeight)

            return when (layoutDir) {
                LayoutDirection.Ltr -> Pair(leftPaneSize, rightPaneSize)
                LayoutDirection.Rtl -> Pair(rightPaneSize, leftPaneSize)
            }
        }
    }

    @VisibleForTesting
    fun getLargeScreenPaneSizes(isPortrait: Boolean, pane1Weight: Float = 0.5f): Pair<Size, Size> {
        if (pane1Weight < 0f || pane1Weight > 1f)
            throw IllegalArgumentException("Pane 1 weight must be between 0 and 1")

        if (isPortrait) {
            val paneWidth = windowWidthDp.value

            val pane1Height = windowHeightDp.value * pane1Weight
            val pane2Height = windowHeightDp.value - pane1Height

            return Pair(Size(paneWidth, pane1Height), Size(paneWidth, pane2Height))
        } else {
            val paneHeight = windowHeightDp.value

            // REVISIT: do we want to add an option for padding between the panes?
            val pane1Width = windowWidthDp.value * pane1Weight
            val pane2Width = windowWidthDp.value - pane1Width

            return Pair(Size(pane1Width, paneHeight), Size(pane2Width, paneHeight))
        }
    }

    @VisibleForTesting
    fun getPaneSizes(isPortrait: Boolean, layoutDir: LayoutDirection, pane1Weight: Float): Pair<Size, Size> {
        return when {
            !calculateWindowMode(isPortrait).isDualScreen -> Pair(Size(0f, 0f), Size(0f, 0f))
            foldIsSeparating -> getFoldablePaneSizes(layoutDir)
            else -> getLargeScreenPaneSizes(isPortrait, pane1Weight)
        }
    }
}
