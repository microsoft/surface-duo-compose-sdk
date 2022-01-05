/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.windowstate

import android.graphics.Rect
import org.junit.Assert.assertEquals
import org.junit.Test

class WindowStateTest {
    private val hasFold = true
    private val foldBounds = Rect(20, 20, 60, 100)
    private val foldState = FoldState.HALF_OPENED
    private val foldSeparates = true
    private val foldOccludes = true
    private val widthSizeClass = WindowSizeClass.MEDIUM
    private val heightSizeClass = WindowSizeClass.EXPANDED

    private val horizontalFold = WindowState(
        hasFold,
        true,
        foldBounds,
        foldState,
        foldSeparates,
        foldOccludes,
        widthSizeClass,
        heightSizeClass
    )
    private val verticalFold = WindowState(
        hasFold,
        false,
        foldBounds,
        foldState,
        foldSeparates,
        foldOccludes,
        widthSizeClass,
        heightSizeClass
    )
    private val noFoldLargeScreen = WindowState(
        widthSizeClass = WindowSizeClass.EXPANDED,
        heightSizeClass = WindowSizeClass.EXPANDED
    )
    private val noFoldCompact = WindowState()

    @Test
    fun returns_correct_fold_size() {
        assertEquals(foldBounds.height(), horizontalFold.foldSize)
        assertEquals(foldBounds.width(), verticalFold.foldSize)
        assertEquals(0, noFoldLargeScreen.foldSize)
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
        assertEquals(WindowMode.DUAL_PORTRAIT, verticalFold.calculateWindowMode(true))
        assertEquals(WindowMode.DUAL_PORTRAIT, verticalFold.calculateWindowMode(false))
    }

    @Test
    fun horizontal_fold_returns_dual_land() {
        assertEquals(WindowMode.DUAL_LANDSCAPE, horizontalFold.calculateWindowMode(true))
        assertEquals(WindowMode.DUAL_LANDSCAPE, horizontalFold.calculateWindowMode(false))
    }
}
