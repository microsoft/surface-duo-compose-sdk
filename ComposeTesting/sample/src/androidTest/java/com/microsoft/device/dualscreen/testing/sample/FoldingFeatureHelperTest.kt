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
import com.microsoft.device.dualscreen.testing.createWindowLayoutInfoPublisherRule
import com.microsoft.device.dualscreen.testing.sample.R
import com.microsoft.device.dualscreen.testing.getString
import com.microsoft.device.dualscreen.testing.simulateHorizontalFoldingFeature
import com.microsoft.device.dualscreen.testing.simulateVerticalFoldingFeature
import com.microsoft.device.dualscreen.testing.sample.ui.theme.ComposeTestingTheme
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule

class FoldingFeatureHelperTest {
    private val composeTestRule = createAndroidComposeRule<MainActivity>()
    private val publisherRule = createWindowLayoutInfoPublisherRule()

    @get: Rule
    val testRule: TestRule

    init {
        testRule = RuleChain.outerRule(publisherRule).around(composeTestRule)
        RuleChain.outerRule(composeTestRule)
    }

    @Test
    fun deviceOpenVertically_showsTwoPanes() {
        composeTestRule.setContent {
            ComposeTestingTheme {
                ComposeTestingApp()
            }
        }
        // Simulate vertical foldingFeature
        publisherRule.simulateVerticalFoldingFeature(composeTestRule)

        // Assert both panes are being shown
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane1_text)).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane2_text)).assertIsDisplayed()
    }

    @Test
    fun deviceOpenHorizontally_showsTwoPanes() {
        composeTestRule.setContent {
            ComposeTestingTheme {
                ComposeTestingApp()
            }
        }
        // Simulate vertical foldingFeature
        publisherRule.simulateHorizontalFoldingFeature(composeTestRule)

        // Assert both panes are being shown
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane1_text)).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane2_text)).assertIsDisplayed()
    }

    @Test
    fun deviceNotOpen_showsOnePane() {
        composeTestRule.setContent {
            ComposeTestingTheme {
                ComposeTestingApp()
            }
        }

        // Assert the first is being shown
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane1_text)).assertIsDisplayed()
        // Assert the second is not being shown
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.pane2_text)).assertDoesNotExist()
    }
}