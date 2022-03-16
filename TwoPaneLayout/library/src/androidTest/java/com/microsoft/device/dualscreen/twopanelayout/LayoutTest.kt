/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.microsoft.device.dualscreen.twopanelayout

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.ViewRootForTest
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.navigation.NavHostController
import com.microsoft.device.dualscreen.windowstate.WindowState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

open class LayoutTest {
    @get:Rule
    val activityTestRule = createAndroidComposeRule<TestActivity>()
    private lateinit var activity: TestActivity
    private lateinit var handler: Handler

    @Before
    fun setup() {
        activity = activityTestRule.activity
        activity.hasFocusLatch.await(5, TimeUnit.SECONDS)
        activityTestRule.runOnUiThread {
            handler = Handler(Looper.getMainLooper())
        }
    }

    @Composable
    internal fun Container(
        modifier: Modifier = Modifier,
        width: Int = 0,
        height: Int = 0,
        content: @Composable () -> Unit
    ) {
        Layout(content, modifier) { measurables, incomingConstraints ->
            val containerConstraints = Constraints(
                minWidth = if (width == 0) incomingConstraints.minWidth else width,
                maxWidth = if (width == 0) incomingConstraints.maxWidth else width,
                minHeight = if (height == 0) incomingConstraints.minHeight else height,
                maxHeight = if (height == 0) incomingConstraints.maxHeight else height
            )
            layout(containerConstraints.maxWidth, containerConstraints.maxHeight) {
                val placeables = measurables.map { measurable ->
                    measurable.measure(containerConstraints)
                }
                placeables.forEach { placeable ->
                    val position = Alignment.Center.align(
                        IntSize(placeable.width, placeable.height),
                        IntSize(containerConstraints.maxWidth, containerConstraints.maxHeight),
                        layoutDirection
                    )
                    placeable.place(
                        position.x, position.y
                    )
                }
            }
        }
    }

    @Composable
    internal fun MockSinglePaneLayout(
        navController: NavHostController,
        firstPane: @Composable TwoPaneScope.() -> Unit,
        secondPane: @Composable TwoPaneScope.() -> Unit
    ) {
        SinglePaneContainer(navController, firstPane, secondPane)
    }

    @Composable
    internal fun MockTwoPaneLayout(
        windowState: WindowState,
        constraints: Constraints,
        firstPane: @Composable TwoPaneScope.() -> Unit,
        secondPane: @Composable TwoPaneScope.() -> Unit
    ) {
        val pane1SizePx: Size
        val pane2SizePx: Size
        with(LocalDensity.current) {
            val pane1SizeDp = windowState.pane1SizeDp
            val pane2SizeDp = windowState.pane2SizeDp
            pane1SizePx = Size(pane1SizeDp.width.toPx(), pane1SizeDp.height.toPx())
            pane2SizePx = Size(pane2SizeDp.width.toPx(), pane2SizeDp.height.toPx())
        }

        val measurePolicy = twoPaneMeasurePolicy(
            windowMode = windowState.windowMode,
            isSeparating = windowState.foldIsSeparating,
            paneSizes = arrayOf(pane1SizePx, pane2SizePx),
            mockConstraints = constraints
        )
        Layout(
            content = {
                TwoPaneScopeInstance.firstPane()
                TwoPaneScopeInstance.secondPane()
            },
            measurePolicy = measurePolicy,
            modifier = Modifier
        )
    }

    internal fun findComposeView(): View {
        return findViewRootForTest(activity).view
    }

    private fun findViewRootForTest(activity: Activity): ViewRootForTest {
        val contentViewGroup = activity.findViewById<ViewGroup>(android.R.id.content)
        return findViewRootForTest(contentViewGroup)!!
    }

    private fun findViewRootForTest(parent: ViewGroup): ViewRootForTest? {
        for (index in 0 until parent.childCount) {
            val child = parent.getChildAt(index)
            if (child is ViewRootForTest) {
                return child
            } else if (child is ViewGroup) {
                val owner = findViewRootForTest(child)
                if (owner != null) {
                    return owner
                }
            }
        }
        return null
    }

    internal fun waitForDraw(view: View) {
        val viewDrawLatch = CountDownLatch(1)
        val listener = ViewTreeObserver.OnDrawListener { viewDrawLatch.countDown() }
        view.post {
            view.viewTreeObserver.addOnDrawListener(listener)
            view.invalidate()
        }
        assertTrue(viewDrawLatch.await(1, TimeUnit.SECONDS))
    }

    internal fun assertEquals(expected: Offset?, actual: Offset?) {
        assertNotNull("Null expected position", expected)
        expected as Offset
        assertNotNull("Null actual position", actual)
        actual as Offset

        assertEquals(
            "Expected x ${expected.x} but obtained ${actual.x}",
            expected.x,
            actual.x,
            0f
        )
        assertEquals(
            "Expected y ${expected.y} but obtained ${actual.y}",
            expected.y,
            actual.y,
            0f
        )
        if (actual.x != actual.x.toInt().toFloat()) {
            fail("Expected integer x coordinate")
        }
        if (actual.y != actual.y.toInt().toFloat()) {
            fail("Expected integer y coordinate")
        }
    }
}
