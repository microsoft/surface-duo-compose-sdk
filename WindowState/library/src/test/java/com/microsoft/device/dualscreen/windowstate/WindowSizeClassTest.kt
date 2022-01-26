/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.windowstate

import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Test

class WindowSizeClassTest {
    @Test
    fun width_returns_compact() {
        assertEquals(WindowSizeClass.COMPACT, getWindowSizeClass(500.dp, Dimension.WIDTH))
        assertEquals(WindowSizeClass.COMPACT, WindowState(windowWidthDp = 500.dp).widthSizeClass())
    }

    @Test
    fun width_returns_medium() {
        assertEquals(WindowSizeClass.MEDIUM, getWindowSizeClass(700.dp, Dimension.WIDTH))
        assertEquals(WindowSizeClass.MEDIUM, WindowState(windowWidthDp = 700.dp).widthSizeClass())
    }

    @Test
    fun width_returns_expanded() {
        assertEquals(WindowSizeClass.EXPANDED, getWindowSizeClass(900.dp, Dimension.WIDTH))
        assertEquals(WindowSizeClass.EXPANDED, WindowState(windowWidthDp = 900.dp).widthSizeClass())
    }

    @Test
    fun height_returns_compact() {
        assertEquals(WindowSizeClass.COMPACT, getWindowSizeClass(300.dp, Dimension.HEIGHT))
        assertEquals(WindowSizeClass.COMPACT, WindowState(windowHeightDp = 300.dp).heightSizeClass())
    }

    @Test
    fun height_returns_medium() {
        assertEquals(WindowSizeClass.MEDIUM, getWindowSizeClass(700.dp, Dimension.HEIGHT))
        assertEquals(WindowSizeClass.MEDIUM, WindowState(windowHeightDp = 700.dp).heightSizeClass())
    }

    @Test
    fun height_returns_expanded() {
        assertEquals(WindowSizeClass.EXPANDED, getWindowSizeClass(1000.dp, Dimension.HEIGHT))
        assertEquals(WindowSizeClass.EXPANDED, WindowState(windowHeightDp = 1000.dp).heightSizeClass())
    }
}
