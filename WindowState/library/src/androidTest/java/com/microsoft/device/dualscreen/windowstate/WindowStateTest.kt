/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.windowstate

import android.graphics.Rect
import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Test

class WindowStateTest {
    private val hasFold = true
    private val foldBounds = Rect(20, 100, 60, 200)
    private val foldState = FoldState.HALF_OPENED
    private val foldSeparates = true
    private val foldOccludes = true

    private val horizontalFoldEqual = WindowState(
        hasFold,
        true,
        foldBounds,
        foldState,
        foldSeparates,
        foldOccludes,
        windowWidthDp = 60.dp,
        windowHeightDp = 300.dp
    )
    private val verticalFoldUnequal = WindowState(
        hasFold,
        false,
        foldBounds,
        foldState,
        foldSeparates,
        foldOccludes,
        windowWidthDp = 100.dp,
        windowHeightDp = 200.dp
    )
    private val noFoldLargeScreen = WindowState(
        windowWidthDp = 850.dp,
        windowHeightDp = 910.dp
    )
    private val noFoldCompact = WindowState()

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
}
