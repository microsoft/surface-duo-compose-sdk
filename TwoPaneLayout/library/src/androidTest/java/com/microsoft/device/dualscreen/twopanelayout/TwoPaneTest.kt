/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.twopanelayout

import android.graphics.Rect
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.microsoft.device.dualscreen.twopanelayout.screenState.DeviceType
import com.microsoft.device.dualscreen.twopanelayout.screenState.LayoutOrientation
import com.microsoft.device.dualscreen.twopanelayout.screenState.LayoutState
import com.microsoft.device.dualscreen.twopanelayout.screenState.ScreenState
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

    @Test
    fun isSinglePaneCheck_withSinglePane() {
        val layoutState = LayoutState.Fold
        val paneMode = TwoPaneMode.TwoPane
        val orientation = LayoutOrientation.Horizontal
        val isSinglePane = isSinglePaneLayout(layoutState, paneMode, orientation)
        assertTrue(isSinglePane)
    }

    @Test
    fun isSinglePaneCheck_withHorizontalSingleMode() {
        val layoutState = LayoutState.Open
        val paneMode = TwoPaneMode.HorizontalSingle
        val orientation = LayoutOrientation.Horizontal
        val isSinglePane = isSinglePaneLayout(layoutState, paneMode, orientation)
        assertTrue(isSinglePane)
    }

    @Test
    fun isSinglePaneCheck_withDualPane() {
        val layoutState = LayoutState.Open
        val paneMode = TwoPaneMode.VerticalSingle
        val orientation = LayoutOrientation.Horizontal
        val isSinglePane = isSinglePaneLayout(layoutState, paneMode, orientation)
        assertTrue(!isSinglePane)
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
                                    childPosition[0] = coordinates.positionInRoot()
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
                                    childPosition[1] = coordinates.positionInRoot()
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
    fun twoPane_withoutWeight() {
        val width = 800
        val height = 600
        val hingeBounds = Rect(390, 0, 410, 600)
        val constraints = Constraints(width, width, height, height)
        val screenState = ScreenState(
            deviceType = DeviceType.Dual,
            screenSize = Size(width.toFloat(), height.toFloat()),
            hingeBounds = hingeBounds,
            orientation = LayoutOrientation.Vertical,
            layoutState = LayoutState.Open
        )

        val drawLatch = CountDownLatch(2)
        val childSize = arrayOfNulls<IntSize>(2)
        val childPosition = arrayOfNulls<Offset>(2)
        activityTestRule.setContent {
            Container(width = width, height = height) {
                MockTwoPaneLayout(
                    screenState = screenState,
                    constraints = constraints,
                    firstPane = {
                        Container(
                            Modifier
                                .onGloballyPositioned { coordinates ->
                                    childSize[0] = coordinates.size
                                    childPosition[0] = coordinates.positionInRoot()
                                    drawLatch.countDown()
                                }
                        ) {}
                    },
                    secondPane = {
                        Container(
                            Modifier
                                .onGloballyPositioned { coordinates ->
                                    childSize[1] = coordinates.size
                                    childPosition[1] = coordinates.positionInRoot()
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
    fun tablet_withWeight() {
        val width = 800
        val height = 1200
        val hingeBounds = Rect()
        val constraints = Constraints(width, width, height, height)
        val screenState = ScreenState(
            deviceType = DeviceType.Big,
            screenSize = Size(width.toFloat(), height.toFloat()),
            hingeBounds = hingeBounds,
            orientation = LayoutOrientation.Horizontal,
            layoutState = LayoutState.Open
        )

        val drawLatch = CountDownLatch(2)
        val childSize = arrayOfNulls<IntSize>(2)
        val childPosition = arrayOfNulls<Offset>(2)
        activityTestRule.setContent {
            Container(width = width, height = height) {
                MockTwoPaneLayout(
                    screenState = screenState,
                    constraints = constraints,
                    firstPane = {
                        Container(
                            Modifier
                                .weight(.4f)
                                .onGloballyPositioned { coordinates ->
                                    childSize[0] = coordinates.size
                                    childPosition[0] = coordinates.positionInRoot()
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
                                    childPosition[1] = coordinates.positionInRoot()
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
