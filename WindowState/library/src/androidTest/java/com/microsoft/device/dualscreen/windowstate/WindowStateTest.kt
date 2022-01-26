/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.windowstate

import android.graphics.RectF
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Test

class WindowStateTest {
    private val hasFold = true
    private val foldBounds = RectF(20f, 100f, 60f, 200f)
    private val foldState = FoldState.HALF_OPENED
    private val foldSeparates = true
    private val foldOccludes = true

    private val horizontalFoldEqual = WindowState(
        hasFold,
        foldIsHorizontal = true,
        foldBounds,
        foldState,
        foldSeparates,
        foldOccludes,
        windowWidthDp = 60.dp,
        windowHeightDp = 300.dp
    )
    private val horizontalFoldEqualNotSeparating = WindowState(
        hasFold,
        foldIsHorizontal = true,
        RectF(0f, 699f, 1000f, 701f),
        FoldState.FLAT,
        foldIsSeparating = false,
        foldIsOccluding = false,
        windowWidthDp = 1000.dp,
        windowHeightDp = 1700.dp
    )
    private val verticalFoldUnequal = WindowState(
        hasFold,
        foldIsHorizontal = false,
        foldBounds,
        foldState,
        foldSeparates,
        foldOccludes,
        windowWidthDp = 100.dp,
        windowHeightDp = 200.dp
    )
    private val verticalFoldUnequalNotSeparating = WindowState(
        hasFold,
        foldIsHorizontal = false,
        foldBounds,
        FoldState.FLAT,
        foldIsSeparating = false,
        foldIsOccluding = false,
        windowWidthDp = 100.dp,
        windowHeightDp = 200.dp
    )
    private val noFoldLargeScreen = WindowState(
        windowWidthDp = 850.dp,
        windowHeightDp = 910.dp
    )
    private val noFoldCompact = WindowState()

    @Test
    fun default_constructor_assigns_correct_values() {
        val windowState = WindowState()

        assertEquals(false, windowState.hasFold)
        assertEquals(false, windowState.foldIsHorizontal)
        assertEquals(RectF(), windowState.foldBoundsDp)
        assertEquals(FoldState.FLAT, windowState.foldState)
        assertEquals(false, windowState.foldIsSeparating)
        assertEquals(false, windowState.foldIsOccluding)
        assertEquals(0.dp, windowState.windowWidthDp)
        assertEquals(0.dp, windowState.windowHeightDp)
    }

    @Test
    fun parameterized_constructor_assigns_correct_values() {
        val windowState = verticalFoldUnequalNotSeparating

        assertEquals(hasFold, windowState.hasFold)
        assertEquals(false, windowState.foldIsHorizontal)
        assertEquals(foldBounds, windowState.foldBoundsDp)
        assertEquals(FoldState.FLAT, windowState.foldState)
        assertEquals(false, windowState.foldIsSeparating)
        assertEquals(false, windowState.foldIsOccluding)
        assertEquals(100.dp, windowState.windowWidthDp)
        assertEquals(200.dp, windowState.windowHeightDp)
    }

    @Test
    fun returns_correct_fold_size() {
        assertEquals(foldBounds.height().dp, horizontalFoldEqual.foldSizeDp)
        assertEquals(foldBounds.width().dp, verticalFoldUnequal.foldSizeDp)
        assertEquals(0.dp, noFoldLargeScreen.foldSizeDp)
    }

    @Test
    fun portrait_large_screen_returns_dual_land() {
        assertEquals(WindowMode.DUAL_LANDSCAPE, noFoldLargeScreen.calculateWindowMode(true))
    }

    @Test
    fun landscape_large_screen_returns_dual_port() {
        assertEquals(WindowMode.DUAL_PORTRAIT, noFoldLargeScreen.calculateWindowMode(false))
    }

    @Test
    fun portrait_compact_returns_single_port() {
        assertEquals(WindowMode.SINGLE_PORTRAIT, noFoldCompact.calculateWindowMode(true))
    }

    @Test
    fun landscape_compact_returns_single_land() {
        assertEquals(WindowMode.SINGLE_LANDSCAPE, noFoldCompact.calculateWindowMode(false))
    }

    @Test
    fun vertical_fold_returns_dual_port() {
        assertEquals(WindowMode.DUAL_PORTRAIT, verticalFoldUnequal.calculateWindowMode(true))
        assertEquals(WindowMode.DUAL_PORTRAIT, verticalFoldUnequal.calculateWindowMode(false))
    }

    @Test
    fun horizontal_fold_returns_dual_land() {
        assertEquals(WindowMode.DUAL_LANDSCAPE, horizontalFoldEqual.calculateWindowMode(true))
        assertEquals(WindowMode.DUAL_LANDSCAPE, horizontalFoldEqual.calculateWindowMode(false))
    }

    @Test
    fun non_separating_compact_fold_returns_single_modes() {
        assertEquals(WindowMode.SINGLE_PORTRAIT, verticalFoldUnequalNotSeparating.calculateWindowMode(true))
        assertEquals(WindowMode.SINGLE_LANDSCAPE, verticalFoldUnequalNotSeparating.calculateWindowMode(false))
    }

    @Test
    fun non_separating_expanded_fold_returns_dual_modes() {
        assertEquals(WindowMode.DUAL_LANDSCAPE, horizontalFoldEqualNotSeparating.calculateWindowMode(true))
        assertEquals(WindowMode.DUAL_PORTRAIT, horizontalFoldEqualNotSeparating.calculateWindowMode(false))
    }

    @Test
    fun equal_panes_returns_same_foldable_pane_size() {
        val paneSizesLtr = horizontalFoldEqual.getFoldablePaneSizes(LayoutDirection.Ltr)
        val paneSizesRtl = horizontalFoldEqual.getFoldablePaneSizes(LayoutDirection.Rtl)

        // Assert that top pane size is 60 x 100 dp
        assertEquals(Size(60f, 100f), paneSizesLtr.first)
        assertEquals(Size(60f, 100f), paneSizesRtl.first)

        // Assert that bottom pane size is also 60 x 100 dp
        assertEquals(Size(60f, 100f), paneSizesLtr.second)
        assertEquals(Size(60f, 100f), paneSizesRtl.second)
    }

    @Test
    fun unequal_panes_return_different_foldable_pane_sizes() {
        val paneSizesLtr = verticalFoldUnequal.getFoldablePaneSizes(LayoutDirection.Ltr)
        val paneSizesRtl = verticalFoldUnequal.getFoldablePaneSizes(LayoutDirection.Rtl)

        // Assert that left pane size is 20 x 200 dp
        assertEquals(Size(20f, 200f), paneSizesLtr.first)
        assertEquals(Size(20f, 200f), paneSizesRtl.second)

        // Assert that right pane size is 40 x 200 dp
        assertEquals(Size(40f, 200f), paneSizesLtr.second)
        assertEquals(Size(40f, 200f), paneSizesRtl.first)
    }

    @Test
    fun no_fold_compact_returns_foldable_pane_size_0() {
        val paneSizesLtr = noFoldCompact.getFoldablePaneSizes(LayoutDirection.Ltr)
        val paneSizesRtl = noFoldCompact.getFoldablePaneSizes(LayoutDirection.Rtl)

        assertEquals(Size(0f, 0f), paneSizesLtr.first)
        assertEquals(Size(0f, 0f), paneSizesLtr.second)
        assertEquals(Size(0f, 0f), paneSizesRtl.first)
        assertEquals(Size(0f, 0f), paneSizesRtl.second)
    }

    @Test
    fun no_fold_large_screen_returns_foldable_pane_size_0() {
        val paneSizesLtr = noFoldLargeScreen.getFoldablePaneSizes(LayoutDirection.Ltr)
        val paneSizesRtl = noFoldLargeScreen.getFoldablePaneSizes(LayoutDirection.Rtl)

        assertEquals(Size(0f, 0f), paneSizesLtr.first)
        assertEquals(Size(0f, 0f), paneSizesLtr.second)
        assertEquals(Size(0f, 0f), paneSizesRtl.first)
        assertEquals(Size(0f, 0f), paneSizesRtl.second)
    }

    @Test
    fun non_separating_fold_returns_foldable_pane_size_0() {
        val paneSizesLtr = verticalFoldUnequalNotSeparating.getFoldablePaneSizes(LayoutDirection.Ltr)
        val paneSizesRtl = verticalFoldUnequalNotSeparating.getFoldablePaneSizes(LayoutDirection.Rtl)

        assertEquals(Size(0f, 0f), paneSizesLtr.first)
        assertEquals(Size(0f, 0f), paneSizesLtr.second)
        assertEquals(Size(0f, 0f), paneSizesRtl.first)
        assertEquals(Size(0f, 0f), paneSizesRtl.second)
    }

    @Test
    fun large_screen_returns_equal_panes_by_default() {
        val paneSizesPortrait = noFoldLargeScreen.getLargeScreenPaneSizes(true)
        val paneSizesLandscape = noFoldLargeScreen.getLargeScreenPaneSizes(false)

        // Assert that default portrait pane size is half the window - 425 x 910 dp
        assertEquals(Size(425f, 910f), paneSizesPortrait.first)
        assertEquals(Size(425f, 910f), paneSizesPortrait.second)

        // Assert that default landscape pane size is half the window - 850 x 455 dp
        assertEquals(Size(850f, 455f), paneSizesLandscape.first)
        assertEquals(Size(850f, 455f), paneSizesLandscape.second)
    }

    @Test
    fun large_screen_returns_weighted_panes() {
        val paneSizesPortrait = noFoldLargeScreen.getLargeScreenPaneSizes(true, 0.25f)
        val paneSizesLandscape = noFoldLargeScreen.getLargeScreenPaneSizes(false, 0.25f)

        // Assert that the portrait pane sizes are split 25/75
        assertEquals(Size(212.5f, 910f), paneSizesPortrait.first)
        assertEquals(Size(637.5f, 910f), paneSizesPortrait.second)

        // Assert that the landscape pane sizes are split 25/75
        assertEquals(Size(850f, 227.5f), paneSizesLandscape.first)
        assertEquals(Size(850f, 682.5f), paneSizesLandscape.second)
    }
}
