/*
 *
 *  * Copyright (c) Microsoft Corporation. All rights reserved.
 *  * Licensed under the MIT License.
 *
 */

package com.microsoft.device.dualscreen.testing.sample

import android.app.UiAutomation
import android.view.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.microsoft.device.dualscreen.testing.DeviceModel
import com.microsoft.device.dualscreen.testing.compose.foldableRuleChain
import com.microsoft.device.dualscreen.testing.compose.getString
import com.microsoft.device.dualscreen.testing.filters.DeviceOrientation
import com.microsoft.device.dualscreen.testing.filters.DualScreenTest
import com.microsoft.device.dualscreen.testing.filters.MockFoldingFeature
import com.microsoft.device.dualscreen.testing.filters.MockFoldingFeature.FoldingFeatureOrientation
import com.microsoft.device.dualscreen.testing.filters.MockFoldingFeature.FoldingFeatureState
import com.microsoft.device.dualscreen.testing.filters.SingleScreenTest
import com.microsoft.device.dualscreen.testing.filters.TargetDevices
import com.microsoft.device.dualscreen.testing.rules.FoldableTestRule
import com.microsoft.device.dualscreen.testing.runner.FoldableJUnit4ClassRunner
import com.microsoft.device.dualscreen.testing.spanFromStart
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith

@RunWith(FoldableJUnit4ClassRunner::class)
class TestSample {
    private val composeTestRule = createAndroidComposeRule<MainActivity>()
    private val foldableTestRule = FoldableTestRule()
    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @get:Rule
    val testRule: TestRule = foldableRuleChain(composeTestRule, foldableTestRule)

    @Before
    fun setUp() {
        composeTestRule.setContent {
            ComposeTestingApp()
        }
    }

    /**
     * ----------------SINGLE AND DUAL SCREEN TESTS----------------
     */

    @Test
    @SingleScreenTest(orientation = UiAutomation.ROTATION_FREEZE_0)
    fun singlePortrait_showsOnePane() {
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane1_text)).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane2_text)).assertDoesNotExist()
    }

    @Test
    @DualScreenTest(orientation = UiAutomation.ROTATION_FREEZE_90)
    fun dualLandscape_showsOnePane() {
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane1_text)).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane2_text)).assertDoesNotExist()
    }

    /**
     * ----------------MOCK FOLDING FEATURE TESTS----------------
     */

    @Test
    @MockFoldingFeature(
        windowBounds = [0, 0, 400, 400],
        center = 0,
        size = 2,
        state = FoldingFeatureState.HALF_OPENED,
        orientation = FoldingFeatureOrientation.HORIZONTAL
    )
    fun horizontalFoldingFeature_showsOnePane() {
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane1_text)).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane2_text)).assertDoesNotExist()
    }

    /**
     * ----------------TARGET DEVICES TESTS----------------
     */

    @Test
    @TargetDevices(devices = [DeviceModel.SurfaceDuo, DeviceModel.SurfaceDuo2])
    fun surfaceDuo_showsTwoPanesAfterSpan() {
        // One pane shown before span
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane1_text)).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane2_text)).assertDoesNotExist()

        // Span
        device.spanFromStart()

        // Two panes shown after span
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane1_text)).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane2_text)).assertIsDisplayed()
    }

    @Test
    @TargetDevices(ignoreDevices = [DeviceModel.FoldOut])
    fun foldInDevices_noFoldingFeature_showOnePane() {
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane1_text)).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane2_text)).assertDoesNotExist()
    }

    /**
     * ----------------DEVICE ORIENTATION TESTS----------------
     */

    @Test
    @DeviceOrientation(orientation = UiAutomation.ROTATION_FREEZE_270)
    fun testIsLandscape() {
        assertEquals(device.displayRotation, Surface.ROTATION_270)
    }
}
