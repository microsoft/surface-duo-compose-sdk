/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.twopanelayout

import android.graphics.Rect
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.IntSize
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.microsoft.device.dualscreen.windowstate.WindowMode
import com.microsoft.device.dualscreen.windowstate.WindowState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

@RunWith(AndroidJUnit4::class)
class TwoPaneTest : LayoutTest() {
    /**
     * Check that isSinglePane returns the correct value for all postures when in TwoPane mode
     */
    @Test
    fun isSinglePaneCheck_withTwoPaneMode() {
        val paneMode = TwoPaneMode.TwoPane

        for (windowMode in WindowMode.values()) {
            val isSinglePane = isSinglePaneLayout(windowMode, paneMode)

            when {
                windowMode.isDualScreen -> assert(!isSinglePane)
                else -> assert(isSinglePane)
            }
        }
    }

    /**
     * Check that isSinglePane returns the correct value for all postures when in HorizontalSingle mode
     */
    @Test
    fun isSinglePaneCheck_withHorizontalSingleMode() {
        val paneMode = TwoPaneMode.HorizontalSingle

        for (windowMode in WindowMode.values()) {
            val isSinglePane = isSinglePaneLayout(windowMode, paneMode)

            when (windowMode) {
                WindowMode.DUAL_PORTRAIT -> assert(!isSinglePane)
                else -> assert(isSinglePane)
            }
        }
    }

    /**
     * Check that isSinglePane returns the correct value for all postures when in VerticalSingle mode
     */
    @Test
    fun isSinglePaneCheck_withVerticalSingleMode() {
        val paneMode = TwoPaneMode.VerticalSingle

        for (windowMode in WindowMode.values()) {
            val isSinglePane = isSinglePaneLayout(windowMode, paneMode)

            when (windowMode) {
                WindowMode.DUAL_LANDSCAPE -> assert(!isSinglePane)
                else -> assert(isSinglePane)
            }
        }
    }

    @Test
    fun singlePane_layout() {
        val width = 400
        val height = 600

        val drawLatch = CountDownLatch(1)
        val childSize = arrayOfNulls<IntSize>(2)
        val childPosition = arrayOfNulls<Offset>(2)
        activityTestRule.setContent {
            Container(width = width, height = height) {
                MockSinglePaneLayout(
                    firstPane =
                    {
                        Container(
                            Modifier
                                .onGloballyPositioned { coordinates ->
                                    childSize[0] = coordinates.size
                                    childPosition[0] = coordinates.positionInParent()
                                    drawLatch.countDown()
                                }
                        ) {}
                    },
                    secondPane =
                    {
                        Container(
                            Modifier
                                .onGloballyPositioned { coordinates ->
                                    childSize[1] = coordinates.size
                                    childPosition[1] = coordinates.positionInParent()
                                    drawLatch.countDown()
                                }
                        ) {}
                    }
                )
            }
        }
        assertTrue(drawLatch.await(1, TimeUnit.SECONDS))

        val root = findComposeView()
        waitForDraw(root)

        assertEquals(IntSize(root.width, root.height), childSize[0])
        assertEquals(Offset(0f, 0f), childPosition[0])
        assertNull(childSize[1])
        assertNull(childPosition[1])
    }

    @Test
    fun twoPaneSeparating_withoutWeight() {
        val width = 800
        val height = 600
        val hingeBounds = Rect(390, 0, 410, 600)
        val constraints = Constraints(width, width, height, height)
        var widthDp: Dp
        var heightDp: Dp
        var hingeBoundsDp: DpRect

        val drawLatch = CountDownLatch(2)
        val childSize = arrayOfNulls<IntSize>(2)
        val childPosition = arrayOfNulls<Offset>(2)
        activityTestRule.setContent {
            with(LocalDensity.current) {
                widthDp = width.toDp()
                heightDp = height.toDp()

                val left = hingeBounds.left.toDp()
                val top = hingeBounds.top.toDp()
                val right = hingeBounds.right.toDp()
                val bottom = hingeBounds.bottom.toDp()

                hingeBoundsDp = DpRect(left, top, right, bottom)
            }

            Container(width = width, height = height) {
                MockTwoPaneLayout(
                    windowState = WindowState(
                        hasFold = true,
                        foldIsHorizontal = false,
                        foldBoundsDp = hingeBoundsDp,
                        foldIsSeparating = true,
                        windowWidthDp = widthDp,
                        windowHeightDp = heightDp,
                    ),
                    constraints = constraints,
                    firstPane = {
                        Container(
                            Modifier
                                .onGloballyPositioned { coordinates ->
                                    childSize[0] = coordinates.size
                                    childPosition[0] = coordinates.positionInParent()
                                    drawLatch.countDown()
                                }
                        ) {}
                    },
                    secondPane = {
                        Container(
                            Modifier
                                .onGloballyPositioned { coordinates ->
                                    childSize[1] = coordinates.size
                                    childPosition[1] = coordinates.positionInParent()
                                    drawLatch.countDown()
                                }
                        ) {}
                    }
                )
            }
        }
        assertTrue(drawLatch.await(1, TimeUnit.SECONDS))

        val root = findComposeView()
        waitForDraw(root)

        assertEquals(IntSize(width, height), IntSize(root.width, root.height))
        assertEquals(IntSize(hingeBounds.left, root.height), childSize[0])
        assertEquals(IntSize(hingeBounds.left, root.height), childSize[1])
        assertEquals(Offset(0f, 0f), childPosition[0])
        assertEquals(Offset(hingeBounds.right.toFloat(), 0f), childPosition[1])
    }

    @Test
    fun twoPaneSeparating_withWeight() {
        val width = 1200
        val height = 1000
        val hingeBounds = Rect(590, 0, 610, 1000)
        val constraints = Constraints(width, width, height, height)
        var widthDp: Dp
        var heightDp: Dp
        var hingeBoundsDp: DpRect

        val drawLatch = CountDownLatch(2)
        val childSize = arrayOfNulls<IntSize>(2)
        val childPosition = arrayOfNulls<Offset>(2)
        activityTestRule.setContent {
            with(LocalDensity.current) {
                widthDp = width.toDp()
                heightDp = height.toDp()

                val left = hingeBounds.left.toDp()
                val top = hingeBounds.top.toDp()
                val right = hingeBounds.right.toDp()
                val bottom = hingeBounds.bottom.toDp()

                hingeBoundsDp = DpRect(left, top, right, bottom)
            }

            Container(width = width, height = height) {
                MockTwoPaneLayout(
                    windowState = WindowState(
                        hasFold = true,
                        foldIsHorizontal = false,
                        foldBoundsDp = hingeBoundsDp,
                        foldIsSeparating = true,
                        windowWidthDp = widthDp,
                        windowHeightDp = heightDp,
                    ),
                    constraints = constraints,
                    firstPane = {
                        Container(
                            Modifier
                                .weight(.3f)
                                .onGloballyPositioned { coordinates ->
                                    childSize[0] = coordinates.size
                                    childPosition[0] = coordinates.positionInParent()
                                    drawLatch.countDown()
                                }
                        ) {}
                    },
                    secondPane = {
                        Container(
                            Modifier
                                .weight(.7f)
                                .onGloballyPositioned { coordinates ->
                                    childSize[1] = coordinates.size
                                    childPosition[1] = coordinates.positionInParent()
                                    drawLatch.countDown()
                                }
                        ) {}
                    }
                )
            }
        }
        assertTrue(drawLatch.await(1, TimeUnit.SECONDS))

        val root = findComposeView()
        waitForDraw(root)

        assertEquals(IntSize(width, height), IntSize(root.width, root.height))
        assertEquals(IntSize(hingeBounds.left, root.height), childSize[0])
        assertEquals(IntSize(hingeBounds.left, root.height), childSize[1])
        assertEquals(Offset(0f, 0f), childPosition[0])
        assertEquals(Offset(hingeBounds.right.toFloat(), 0f), childPosition[1])
    }

    @Test
    fun twoPaneNotSeparating_withWeight() {
        val width = 1200
        val height = 800
        val hingeBounds = Rect(598, 0, 601, 800)
        val constraints = Constraints(width, width, height, height)
        var widthDp: Dp
        var heightDp: Dp
        var hingeBoundsDp: DpRect

        val drawLatch = CountDownLatch(2)
        val childSize = arrayOfNulls<IntSize>(2)
        val childPosition = arrayOfNulls<Offset>(2)
        activityTestRule.setContent {
            with(LocalDensity.current) {
                widthDp = width.toDp()
                heightDp = height.toDp()

                val left = hingeBounds.left.toDp()
                val top = hingeBounds.top.toDp()
                val right = hingeBounds.right.toDp()
                val bottom = hingeBounds.bottom.toDp()

                hingeBoundsDp = DpRect(left, top, right, bottom)
            }

            Container(width = width, height = height) {
                MockTwoPaneLayout(
                    windowState = WindowState(
                        hasFold = true,
                        foldIsHorizontal = false,
                        foldBoundsDp = hingeBoundsDp,
                        foldIsSeparating = false,
                        windowWidthDp = widthDp,
                        windowHeightDp = heightDp,
                    ),
                    constraints = constraints,
                    firstPane = {
                        Container(
                            Modifier
                                .weight(.3f)
                                .onGloballyPositioned { coordinates ->
                                    childSize[0] = coordinates.size
                                    childPosition[0] = coordinates.positionInParent()
                                    drawLatch.countDown()
                                }
                        ) {}
                    },
                    secondPane = {
                        Container(
                            Modifier
                                .weight(.7f)
                                .onGloballyPositioned { coordinates ->
                                    childSize[1] = coordinates.size
                                    childPosition[1] = coordinates.positionInParent()
                                    drawLatch.countDown()
                                }
                        ) {}
                    }
                )
            }
        }
        assertTrue(drawLatch.await(1, TimeUnit.SECONDS))

        val root = findComposeView()
        waitForDraw(root)

        assertEquals(IntSize(width, height), IntSize(root.width, root.height))
        assertEquals(IntSize(width = (width * .3f).roundToInt(), height = height), childSize[0])
        assertEquals(IntSize(width = (width * .7f).roundToInt(), height = height), childSize[1])
        assertEquals(Offset(0f, 0f), childPosition[0])
        assertEquals(Offset(width * 0.3f, height.toFloat()), childPosition[1])
    }

    @Test
    fun tablet_withWeight() {
        val width = 3300
        val height = 4000
        val constraints = Constraints(width, width, height, height)

        var widthDp: Dp
        var heightDp: Dp

        val drawLatch = CountDownLatch(2)
        val childSize = arrayOfNulls<IntSize>(2)
        val childPosition = arrayOfNulls<Offset>(2)
        activityTestRule.setContent {
            with(LocalDensity.current) {
                widthDp = width.toDp()
                heightDp = height.toDp()
            }

            Container(width = width, height = height) {
                MockTwoPaneLayout(
                    windowState = WindowState(
                        windowWidthDp = widthDp,
                        windowHeightDp = heightDp,
                    ),
                    constraints = constraints,
                    firstPane = {
                        Container(
                            Modifier
                                .weight(.4f)
                                .onGloballyPositioned { coordinates ->
                                    childSize[0] = coordinates.size
                                    childPosition[0] = coordinates.positionInParent()
                                    drawLatch.countDown()
                                }
                        ) {}
                    },
                    secondPane = {
                        Container(
                            Modifier
                                .weight(.6f)
                                .onGloballyPositioned { coordinates ->
                                    childSize[1] = coordinates.size
                                    childPosition[1] = coordinates.positionInParent()
                                    drawLatch.countDown()
                                }
                        ) {}
                    }
                )
            }
        }
        assertTrue(drawLatch.await(1, TimeUnit.SECONDS))

        val root = findComposeView()
        waitForDraw(root)

        assertEquals(IntSize(width = width, height = (height * .4f).roundToInt()), childSize[0])
        assertEquals(IntSize(width = width, height = (height * .6f).roundToInt()), childSize[1])
        assertEquals(Offset(0f, 0f), childPosition[0])
        assertEquals(Offset(0f, height * .4f), childPosition[1])
    }
}
