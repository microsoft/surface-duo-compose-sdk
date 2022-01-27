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
     * Dp value of the width of the hinge or the folding line if it is separating, otherwise 0
     */
    val foldSizeDp: Dp =
        if (foldIsSeparating) {
            if (foldIsHorizontal) foldBoundsDp.height().dp else foldBoundsDp.width().dp
        } else {
            0.dp
        }

    /**
     * Current window mode (single portrait, single landscape, dual portrait, or dual landscape)
     */
    val windowMode: WindowMode
        @Composable get() {
            val isPortrait = windowIsPortrait()

            return calculateWindowMode(isPortrait)
        }

    /**
     * Returns whether the current window is in a dual screen mode or not.
     *
     * A window is considered dual screen if it's a large screen ("expanded" width size class) or a foldable
     * (folding feature is present and is separating)
     *
     * @return true if dual screen, false otherwise
     */
    @Composable
    fun isDualScreen(): Boolean {
        return windowMode.isDualScreen
    }

    /**
     * Returns whether the current window is in dual portrait mode or not
     *
     * A window is considered dual screen if it's a large screen ("expanded" width size class) or a foldable
     * (folding feature is present and is separating)
     *
     * @return true if in dual portrait mode, false otherwise
     */
    @Composable
    fun isDualPortrait(): Boolean {
        return windowMode == WindowMode.DUAL_PORTRAIT
    }

    /**
     * Returns whether the current window is in dual landscape mode or not
     *
     * A window is considered dual screen if it's a large screen ("expanded" width size class) or a foldable
     * (folding feature is present and is separating)
     *
     * @return true if in dual landscape mode, false otherwise
     */
    @Composable
    fun isDualLandscape(): Boolean {
        return windowMode == WindowMode.DUAL_LANDSCAPE
    }

    /**
     * Returns whether the current window is in single portrait mode or not
     *
     * @return true if in single portrait mode, false otherwise
     */
    @Composable
    fun isSinglePortrait(): Boolean {
        return windowMode == WindowMode.SINGLE_PORTRAIT
    }

    /**
     * Returns whether the current window is in single landscape mode or not
     *
     * @return true if in single landscape mode, false otherwise
     */
    @Composable
    fun isSingleLandscape(): Boolean {
        return windowMode == WindowMode.SINGLE_LANDSCAPE
    }

    /**
     * Returns the size class (compact, medium, or expanded) for the window width
     *
     * @return width size class
     */
    fun widthSizeClass(): WindowSizeClass {
        return getWindowSizeClass(windowWidthDp)
    }

    /**
     * Returns the size class (compact, medium, or expanded) for the window height
     *
     * @return height size class
     */
    fun heightSizeClass(): WindowSizeClass {
        return getWindowSizeClass(windowHeightDp, Dimension.HEIGHT)
    }

    /**
     * Dp size of pane 1 (top and left or right pane depending on local layout direction) when a fold
     * is separating, otherwise the size is zero
     */
    val foldablePane1SizeDp: Size
        @Composable get() {
            return getFoldablePaneSizes(LocalLayoutDirection.current).first
        }

    /**
     * Dp size of pane 2 (bottom and left or right pane depending on local layout direction) when a
     * fold is separating, otherwise the size is zero
     */
    val foldablePane2SizeDp: Size
        @Composable get() {
            return getFoldablePaneSizes(LocalLayoutDirection.current).second
        }

    /**
     * Returns the dp size of pane 1 (top and left or right pane depending on local layout direction) when a fold
     * is separating or the window is large, otherwise the returned size is zero. If a separating fold is present,
     * then the pane1Weight parameter is ignored and the panes are split according to the fold boundaries.
     *
     * @param pane1Weight: the proportion of the window that pane 1 should occupy (must be between 0 and 1),
     * default weight is 0.5 to make equal panes
     * @return dp size of pane 1
     */
    @Composable
    fun pane1SizeDp(pane1Weight: Float = 0.5f): Size {
        return getPaneSizes(windowIsPortrait(), LocalLayoutDirection.current, pane1Weight).first
    }

    /**
     * Returns the dp size of pane 2 (bottom and left or right pane depending on local layout direction) when a
     * fold is separating or the window is large, otherwise the returned size is zero. If a separating fold is
     * present, then the pane1Weight parameter is ignored and the panes are split according to the fold boundaries.
     *
     * @param pane1Weight: the proportion of the window that pane 1 should occupy (must be between 0 and 1),
     * default weight is 0.5 to make equal panes
     * @return dp size of pane 2
     */
    @Composable
    fun pane2SizeDp(pane1Weight: Float = 0.5f): Size {
        return getPaneSizes(windowIsPortrait(), LocalLayoutDirection.current, pane1Weight).second
    }

    /**
     * Checks whether the window is in the portrait or landscape orientation
     *
     * @return true if portrait, false if landscape
     */
    @Composable
    private fun windowIsPortrait(): Boolean {
        // REVISIT: should width/height ratio of the window be used instead of orientation?
        return LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    /**
     * Checks whether the window is considered large (expanded width size class) or not. Note: currently,
     * foldables take priority over large screens, so if a window has a separating fold and is a large windnow, it
     * will be treated as a foldable.
     *
     * @return true if large, false otherwise
     */
    private fun windowIsLarge(): Boolean {
        // REVISIT: should height class also be considered?
        // Also, right now we are considering large screens + foldables mutually exclusive
        // (which seems necessary for dualscreen apps), but we may want to think about this
        // more and change our approach if we think there are cases where we want an app to
        // know about both properties
        val widthSizeClass = getWindowSizeClass(windowWidthDp)
        return !foldIsSeparating && widthSizeClass == WindowSizeClass.EXPANDED
    }

    /**
     * Calculates the current window mode
     *
     * @param isPortrait: whether the window is in the portrait orientation or not
     * @return current window mode
     */
    @VisibleForTesting
    internal fun calculateWindowMode(isPortrait: Boolean): WindowMode {
        return when {
            foldIsSeparating -> if (foldIsHorizontal) WindowMode.DUAL_LANDSCAPE else WindowMode.DUAL_PORTRAIT
            windowIsLarge() -> if (isPortrait) WindowMode.DUAL_LANDSCAPE else WindowMode.DUAL_PORTRAIT
            else -> if (isPortrait) WindowMode.SINGLE_PORTRAIT else WindowMode.SINGLE_LANDSCAPE
        }
    }

    /**
     * Calculates pane sizes based on the boundaries of a separating fold. If no separating folds are present,
     * then the returned sizes are zero.
     *
     * @param layoutDir: language layout direction that determines whether the right or left pane is considered
     * pane 1 (primary pane)
     * @return pair of sizes, with pane 1 being the first size and pane 2 being the second
     */
    @VisibleForTesting
    internal fun getFoldablePaneSizes(layoutDir: LayoutDirection): Pair<Size, Size> {
        // If a separating fold is not present, return size zero
        if (!foldIsSeparating)
            return Pair(Size(0f, 0f), Size(0f, 0f))

        if (foldIsHorizontal) {
            // When a fold is horizontal, pane widths are equal and pane heights depend on the fold boundaries
            val paneWidth = windowWidthDp.value

            val topPaneHeight = foldBoundsDp.top
            val bottomPaneHeight = windowHeightDp.value - foldBoundsDp.bottom

            // The top pane is always considered pane 1
            val topPaneSize = Size(paneWidth, topPaneHeight)
            val bottomPaneSize = Size(paneWidth, bottomPaneHeight)

            return Pair(topPaneSize, bottomPaneSize)
        } else {
            // When a fold is vertical, pane heights are equal and pane widths depend on the fold boundaries
            val paneHeight = windowHeightDp.value

            val leftPaneWidth = foldBoundsDp.left
            val rightPaneWidth = windowWidthDp.value - foldBoundsDp.right

            val leftPaneSize = Size(leftPaneWidth, paneHeight)
            val rightPaneSize = Size(rightPaneWidth, paneHeight)

            // Pane 1 can be right or left depending on the language layout direction
            return when (layoutDir) {
                LayoutDirection.Ltr -> Pair(leftPaneSize, rightPaneSize)
                LayoutDirection.Rtl -> Pair(rightPaneSize, leftPaneSize)
            }
        }
    }

    /**
     * Calculates pane sizes based on window size and pane weight. If the window is not large, then the returned
     * sizes are zero.
     *
     * @param isPortrait: true if the window is in the portrait orientation, false otherwise
     * @param pane1Weight: the proportion of the window that pane 1 should occupy (must be between 0 and 1),
     * default weight is 0.5 to make equal panes
     * @return pair of sizes, with pane 1 being the first size and pane 2 being the second
     */
    @VisibleForTesting
    internal fun getLargeScreenPaneSizes(isPortrait: Boolean, pane1Weight: Float = 0.5f): Pair<Size, Size> {
        // Check that 0 < weight < 1
        if (pane1Weight <= 0f || pane1Weight >= 1f)
            throw IllegalArgumentException("Pane 1 weight must be between 0 and 1")

        // If the window is not large, return size zero
        if (!windowIsLarge())
            return Pair(Size(0f, 0f), Size(0f, 0f))

        if (isPortrait) {
            // When a window is in the portrait orientation, panes should be divided between top and bottom.
            // This means pane widths are equal and pane heights are based on weight (dual landscape)
            val paneWidth = windowWidthDp.value

            val pane1Height = windowHeightDp.value * pane1Weight
            val pane2Height = windowHeightDp.value - pane1Height

            return Pair(Size(paneWidth, pane1Height), Size(paneWidth, pane2Height))
        } else {
            // When a window is in the landscape orientation, panes should be divided between left and right.
            // This means pane heights are equal and pane widths are based on weight (dual portrait)
            val paneHeight = windowHeightDp.value

            // REVISIT: do we want to add an option for padding between the panes?
            val pane1Width = windowWidthDp.value * pane1Weight
            val pane2Width = windowWidthDp.value - pane1Width

            return Pair(Size(pane1Width, paneHeight), Size(pane2Width, paneHeight))
        }
    }

    /**
     * Calculates pane sizes based on all window properties. If no separating folds are present and the window
     * is not large, then the returned sizes are zero.
     *
     * @param isPortrait: true if the window is in the portrait orientation, false otherwise
     * @param layoutDir: language layout direction that determines whether the right or left pane is considered
     * pane 1 (primary pane)
     * @param pane1Weight: the proportion of the window that pane 1 should occupy (must be between 0 and 1),
     * default weight is 0.5 to make equal panes
     * @return pair of sizes, with pane 1 being the first size and pane 2 being the second
     */
    @VisibleForTesting
    internal fun getPaneSizes(isPortrait: Boolean, layoutDir: LayoutDirection, pane1Weight: Float): Pair<Size, Size> {
        return when {
            foldIsSeparating -> getFoldablePaneSizes(layoutDir)
            windowIsLarge() -> getLargeScreenPaneSizes(isPortrait, pane1Weight)
            else -> Pair(Size(0f, 0f), Size(0f, 0f))
        }
    }
}
