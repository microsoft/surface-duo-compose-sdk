/*
 *
 *  * Copyright (c) Microsoft Corporation. All rights reserved.
 *  * Licensed under the MIT License.
 *
 */

package com.microsoft.device.dualscreen.testing.sample

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.microsoft.device.dualscreen.testing.getString
import com.microsoft.device.dualscreen.testing.isSurfaceDuo
import com.microsoft.device.dualscreen.testing.sample.ui.theme.ComposeTestingTheme
import com.microsoft.device.dualscreen.testing.spanFromStart
import com.microsoft.device.dualscreen.testing.unspanToEnd
import org.junit.Rule
import org.junit.Test

class SwipeHelperTest {
    @get: Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @Test
    fun span_showsTwoPanes() {
        composeTestRule.setContent {
            ComposeTestingTheme {
                ComposeTestingApp()
            }
        }

        // Assert that only pane 1 is shown
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane1_text)).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane2_text)).assertDoesNotExist()

        // Span the app from start
        if (device.isSurfaceDuo()) {
            device.spanFromStart()
        } else {
            return
        }

        // Assert that both panes are now shown
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane1_text)).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane2_text)).assertIsDisplayed()
    }

    @Test
    fun unspan_showsOnePane() {
        composeTestRule.setContent {
            ComposeTestingTheme {
                ComposeTestingApp()
            }
        }
        if (device.isSurfaceDuo()) {
            // Span the app from start
            device.spanFromStart()
        } else {
            return
        }

        // Assert that both panes are shown
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane1_text)).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane2_text)).assertIsDisplayed()

        // Unspan the app to the end
        if (device.isSurfaceDuo()) {
            device.unspanToEnd()
        } else {
            return
        }

        // Assert that only pane 1 is now shown
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane1_text)).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane2_text)).assertDoesNotExist()
    }
}
