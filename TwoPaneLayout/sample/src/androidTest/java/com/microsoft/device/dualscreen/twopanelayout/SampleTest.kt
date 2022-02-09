/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.twopanelayout

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.microsoft.device.dualscreen.testing.createWindowLayoutInfoPublisherRule
import com.microsoft.device.dualscreen.testing.getString
import com.microsoft.device.dualscreen.testing.simulateHorizontalFoldingFeature
import com.microsoft.device.dualscreen.testing.simulateVerticalFoldingFeature
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule

class SampleTest {
    private val publisherRule = createWindowLayoutInfoPublisherRule()
    private val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get: Rule
    val testRule: TestRule

    init {
        testRule = RuleChain.outerRule(publisherRule).around(composeTestRule)
        RuleChain.outerRule(composeTestRule)
    }

    @Before
    fun setUp() {
        composeTestRule.setContent {
            MainPage()
        }
    }

    @Test
    fun app_launches() {
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.app_name)).assertIsDisplayed()
    }

    @Test
    fun app_canNavigateToSecondPane() {
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.first_pane_text)).performClick()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.second_pane_text)).assertIsDisplayed()
    }

    @Test
    fun app_canNavigateToFirstPane() {
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.first_pane_text)).performClick()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.second_pane_text)).performClick()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.first_pane_text)).assertIsDisplayed()
    }

    @Test
    fun app_dualPortrait_showsTwoPanes() {
        publisherRule.simulateVerticalFoldingFeature(composeTestRule)

        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.first_pane_text)).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.second_pane_text)).assertIsDisplayed()
    }

    @Test
    fun app_dualLandscape_showsOnePane() {
        publisherRule.simulateHorizontalFoldingFeature(composeTestRule)

        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.first_pane_text)).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.second_pane_text)).assertDoesNotExist()
    }
}
