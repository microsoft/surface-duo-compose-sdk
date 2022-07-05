/*
 *
 *  * Copyright (c) Microsoft Corporation. All rights reserved.
 *  * Licensed under the MIT License.
 *
 */

package com.microsoft.device.dualscreen.testing.sample

import android.app.UiAutomation
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith

/**
 * Test class that shows examples of how to use Test Kit annotations:
 * - @SingleScreenTest
 * - @DualScreenTest
 * - @DeviceOrientation
 * - @MockFoldingFeature
 * - @TargetDevices
 */
@RunWith(FoldableJUnit4ClassRunner::class)
class AnnotationTest {
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
     * Uses @SingleScreenTest annotation to check that sample shows only pane 1 text when in single portrait mode
     */
    @Test
    @SingleScreenTest(orientation = UiAutomation.ROTATION_FREEZE_0)
    fun singlePortrait_showsOnePane() {
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane1_text)).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane2_text)).assertDoesNotExist()
    }

    /**
     * Uses @DualScreenTest annotation to check that sample shows only pane 1 text when in dual landscape mode
     * (as expected when using HorizontalSingle mode for TwoPaneLayout)
     */
    @Test
    @DualScreenTest(orientation = UiAutomation.ROTATION_FREEZE_90)
    fun dualLandscape_showsOnePane() {
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane1_text)).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane2_text)).assertDoesNotExist()
    }

    /**
     * Uses @DualScreenTest annotation to check that sample shows pane 1 and pane 2 text when in dual portrait
     * mode
     *
     * Also uses @TargetDevices annotation with ignoreDevices parameter to exclude this test from running on
     * Surface Duo devices
     */
    @Test
    @DualScreenTest
    @TargetDevices(ignoreDevices = [DeviceModel.SurfaceDuo, DeviceModel.SurfaceDuo2])
    fun foldables_dualPortrait_showsTwoPanes() {
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane1_text)).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane2_text)).assertIsDisplayed()
    }

    /**
     * Checks that only pane 1 text shows at first in single portrait mode, spans the app to dual portrait mode,
     * then checks that samples shows both pane 1 and pane 2 text
     *
     * Uses @TargetDevices annotation with devices parameter to ensure this test only runs on Surface Duo devices
     *
     * Also uses @DeviceOrientation annotation to lock the device in the portrait orientation
     */
    @Test
    @DeviceOrientation(orientation = UiAutomation.ROTATION_FREEZE_0)
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

    /**
     * Checks that sample only shows pane 1 text in when a horizontal folding feature is present
     *
     * Uses @MockFoldingFeature annotation to simulate a horizontal folding feature
     */
    @Test
    @MockFoldingFeature(
        center = 0,
        size = 2,
        state = FoldingFeatureState.HALF_OPENED,
        orientation = FoldingFeatureOrientation.HORIZONTAL
    )
    fun horizontalFoldingFeature_showsOnePane() {
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane1_text)).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane2_text)).assertDoesNotExist()
    }
}
