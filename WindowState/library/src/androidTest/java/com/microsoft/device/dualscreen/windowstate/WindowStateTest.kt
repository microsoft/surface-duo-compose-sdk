/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.windowstate

import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.height
import androidx.compose.ui.unit.width
import org.junit.Assert.assertEquals
import org.junit.Test

class WindowStateTest {
    private val hasFold = true
    private val foldBounds = DpRect(20.dp, 100.dp, 60.dp, 200.dp)
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
        DpRect(0.dp, 699.dp, 1000.dp, 701.dp),
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
    private val noFoldMediumWidthCompactHeight = WindowState(
        windowWidthDp = 700.dp,
        windowHeightDp = 300.dp
    )
    private val noFoldMediumWidthMediumHeight = WindowState(
        windowWidthDp = 700.dp,
        windowHeightDp = 500.dp
    )
    private val noFoldExpandedWidthCompactHeight = WindowState(
        windowWidthDp = 900.dp,
        windowHeightDp = 450.dp
    )
    private val noFoldExpandedWidthMediumHeight = WindowState(
        windowWidthDp = 900.dp,
        windowHeightDp = 600.dp
    )

    /**
     * constructor tests
     * -----------------
     */

    @Test
    fun default_constructor_assigns_correct_values() {
        val windowState = WindowState()

        assertEquals(false, windowState.hasFold)
        assertEquals(false, windowState.foldIsHorizontal)
        assertEquals(DpRect(0.dp, 0.dp, 0.dp, 0.dp), windowState.foldBoundsDp)
        assertEquals(FoldState.FLAT, windowState.foldState)
        assertEquals(false, windowState.foldIsSeparating)
        assertEquals(false, windowState.foldIsOccluding)
        assertEquals(1.dp, windowState.windowWidthDp)
        assertEquals(1.dp, windowState.windowHeightDp)
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

    /**
     * foldSizeDp tests
     * ----------------
     */

    @Test
    fun returns_correct_fold_size() {
        assertEquals(foldBounds.height, horizontalFoldEqual.foldSizeDp)
        assertEquals(foldBounds.width, verticalFoldUnequal.foldSizeDp)
        assertEquals(0.dp, noFoldLargeScreen.foldSizeDp)
    }

    /**
     * calculateWindowMode() tests
     * ---------------------------
     */

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
    fun non_separating_medium_width_compact_height_fold_returns_single_modes() {
        assertEquals(WindowMode.SINGLE_PORTRAIT, noFoldMediumWidthCompactHeight.calculateWindowMode(true))
        assertEquals(WindowMode.SINGLE_LANDSCAPE, noFoldMediumWidthCompactHeight.calculateWindowMode(false))
    }

    @Test
    fun non_separating_medium_width_medium_height_fold_returns_single_modes() {
        assertEquals(WindowMode.SINGLE_PORTRAIT, noFoldMediumWidthMediumHeight.calculateWindowMode(true))
        assertEquals(WindowMode.SINGLE_LANDSCAPE, noFoldMediumWidthMediumHeight.calculateWindowMode(false))
    }

    @Test
    fun non_separating_expanded_width_compact_height_fold_returns_single_modes() {
        assertEquals(WindowMode.SINGLE_PORTRAIT, noFoldExpandedWidthCompactHeight.calculateWindowMode(true))
        assertEquals(WindowMode.SINGLE_LANDSCAPE, noFoldExpandedWidthCompactHeight.calculateWindowMode(false))
    }

    @Test
    fun non_separating_expanded_width_medium_height_fold_returns_dual_modes() {
        assertEquals(WindowMode.DUAL_LANDSCAPE, noFoldExpandedWidthMediumHeight.calculateWindowMode(true))
        assertEquals(WindowMode.DUAL_PORTRAIT, noFoldExpandedWidthMediumHeight.calculateWindowMode(false))
    }

    /**
     * getFoldablePaneSizes() tests
     * ----------------------------
     */

    @Test
    fun equal_panes_returns_same_foldable_pane_size() {
        val paneSizesLtr = horizontalFoldEqual.getFoldablePaneSizes(LayoutDirection.Ltr)
        val paneSizesRtl = horizontalFoldEqual.getFoldablePaneSizes(LayoutDirection.Rtl)

        // Assert that top pane size is 60 x 100 dp
        assertEquals(DpSize(60.dp, 100.dp), paneSizesLtr.first)
        assertEquals(DpSize(60.dp, 100.dp), paneSizesRtl.first)

        // Assert that bottom pane size is also 60 x 100 dp
        assertEquals(DpSize(60.dp, 100.dp), paneSizesLtr.second)
        assertEquals(DpSize(60.dp, 100.dp), paneSizesRtl.second)
    }

    @Test
    fun unequal_panes_return_different_foldable_pane_sizes() {
        val paneSizesLtr = verticalFoldUnequal.getFoldablePaneSizes(LayoutDirection.Ltr)
        val paneSizesRtl = verticalFoldUnequal.getFoldablePaneSizes(LayoutDirection.Rtl)

        // Assert that left pane size is 20 x 200 dp
        assertEquals(DpSize(20.dp, 200.dp), paneSizesLtr.first)
        assertEquals(DpSize(20.dp, 200.dp), paneSizesRtl.second)

        // Assert that right pane size is 40 x 200 dp
        assertEquals(DpSize(40.dp, 200.dp), paneSizesLtr.second)
        assertEquals(DpSize(40.dp, 200.dp), paneSizesRtl.first)
    }

    @Test
    fun no_fold_compact_returns_foldable_pane_size_0() {
        val paneSizesLtr = noFoldCompact.getFoldablePaneSizes(LayoutDirection.Ltr)
        val paneSizesRtl = noFoldCompact.getFoldablePaneSizes(LayoutDirection.Rtl)

        assertEquals(DpSize(0.dp, 0.dp), paneSizesLtr.first)
        assertEquals(DpSize(0.dp, 0.dp), paneSizesLtr.second)
        assertEquals(DpSize(0.dp, 0.dp), paneSizesRtl.first)
        assertEquals(DpSize(0.dp, 0.dp), paneSizesRtl.second)
    }

    @Test
    fun no_fold_large_screen_returns_foldable_pane_size_0() {
        val paneSizesLtr = noFoldLargeScreen.getFoldablePaneSizes(LayoutDirection.Ltr)
        val paneSizesRtl = noFoldLargeScreen.getFoldablePaneSizes(LayoutDirection.Rtl)

        assertEquals(DpSize(0.dp, 0.dp), paneSizesLtr.first)
        assertEquals(DpSize(0.dp, 0.dp), paneSizesLtr.second)
        assertEquals(DpSize(0.dp, 0.dp), paneSizesRtl.first)
        assertEquals(DpSize(0.dp, 0.dp), paneSizesRtl.second)
    }

    @Test
    fun non_separating_fold_returns_foldable_pane_size_0() {
        val paneSizesLtr = verticalFoldUnequalNotSeparating.getFoldablePaneSizes(LayoutDirection.Ltr)
        val paneSizesRtl = verticalFoldUnequalNotSeparating.getFoldablePaneSizes(LayoutDirection.Rtl)

        assertEquals(DpSize(0.dp, 0.dp), paneSizesLtr.first)
        assertEquals(DpSize(0.dp, 0.dp), paneSizesLtr.second)
        assertEquals(DpSize(0.dp, 0.dp), paneSizesRtl.first)
        assertEquals(DpSize(0.dp, 0.dp), paneSizesRtl.second)
    }

    /**
     * getLargeScreenPaneSizes() tests
     * -------------------------------
     */

    @Test
    fun large_screen_returns_equal_panes_by_default() {
        val paneSizesPortrait = noFoldLargeScreen.getLargeScreenPaneSizes(true)
        val paneSizesLandscape = noFoldLargeScreen.getLargeScreenPaneSizes(false)

        // Assert that default portrait pane size (dual landscape) is half the window - 850 x 455 dp
        assertEquals(DpSize(850.dp, 455.dp), paneSizesPortrait.first)
        assertEquals(DpSize(850.dp, 455.dp), paneSizesPortrait.second)

        // Assert that default landscape pane size (dual portrait) is half the window - 425 x 910 dp
        assertEquals(DpSize(425.dp, 910.dp), paneSizesLandscape.first)
        assertEquals(DpSize(425.dp, 910.dp), paneSizesLandscape.second)
    }

    @Test
    fun large_screen_returns_weighted_panes() {
        val paneSizesPortrait = noFoldLargeScreen.getLargeScreenPaneSizes(true, 0.25f)
        val paneSizesLandscape = noFoldLargeScreen.getLargeScreenPaneSizes(false, 0.25f)

        // Assert that the portrait pane sizes (dual landscape) are split 25/75
        assertEquals(DpSize(850.dp, 227.5.dp), paneSizesPortrait.first)
        assertEquals(DpSize(850.dp, 682.5.dp), paneSizesPortrait.second)

        // Assert that the landscape pane sizes (dual portrait) are split 25/75
        assertEquals(DpSize(212.5.dp, 910.dp), paneSizesLandscape.first)
        assertEquals(DpSize(637.5.dp, 910.dp), paneSizesLandscape.second)
    }

    @Test(expected = IllegalArgumentException::class)
    fun weight_less_than_0_throws_exception() {
        noFoldLargeScreen.getLargeScreenPaneSizes(true, -1f)
    }

    @Test(expected = IllegalArgumentException::class)
    fun weight_0_throws_exception() {
        noFoldLargeScreen.getLargeScreenPaneSizes(true, 0.00f)
    }

    @Test(expected = IllegalArgumentException::class)
    fun weight_greater_than_1_throws_exception() {
        noFoldLargeScreen.getLargeScreenPaneSizes(true, 1.01f)
    }

    @Test(expected = IllegalArgumentException::class)
    fun weight_1_throws_exception() {
        noFoldLargeScreen.getLargeScreenPaneSizes(true, 1.00f)
    }

    /**
     * largeScreenPane1Weight tests
     * -------------------------------
     */
    @Test(expected = IllegalArgumentException::class)
    fun set_weight_less_than_0_throws_exception() {
        WindowState().largeScreenPane1Weight = -1f
    }

    @Test(expected = IllegalArgumentException::class)
    fun set_weight_0_throws_exception() {
        WindowState().largeScreenPane1Weight = 0.00f
    }

    @Test(expected = IllegalArgumentException::class)
    fun set_weight_greater_than_1_throws_exception() {
        WindowState().largeScreenPane1Weight = 1.01f
    }

    @Test(expected = IllegalArgumentException::class)
    fun set_weight_1_throws_exception() {
        WindowState().largeScreenPane1Weight = 1.00f
    }

    @Test
    fun weight_default_is_correct() {
        assertEquals(0.5f, WindowState().largeScreenPane1Weight)
    }

    @Test
    fun weight_sets_and_gets() {
        val windowState = WindowState()

        assertEquals(0.5f, windowState.largeScreenPane1Weight)
        windowState.largeScreenPane1Weight = 0.7f
        assertEquals(0.7f, windowState.largeScreenPane1Weight)
    }
}
