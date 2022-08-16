/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.twopanelayout

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.microsoft.device.dualscreen.testing.compose.getString
import com.microsoft.device.dualscreen.testing.compose.simulateHorizontalFoldingFeature
import com.microsoft.device.dualscreen.testing.compose.simulateVerticalFoldingFeature
import com.microsoft.device.dualscreen.testing.createWindowLayoutInfoPublisherRule
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule

class NavSampleTest {
    private val publisherRule = createWindowLayoutInfoPublisherRule()
    private val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get: Rule
    val testRule: TestRule

    init {
        testRule = RuleChain.outerRule(publisherRule).around(composeTestRule)
        RuleChain.outerRule(composeTestRule)
    }

    @Test
    fun app_launches() {
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.app_name)).assertIsDisplayed()
    }

    @Test
    fun app_canNavigateToAllDestinations() {
        // to Destination 2
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.first_dest_text)).performClick()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.second_dest_text)).assertIsDisplayed()

        // to Destination 3
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.second_dest_text)).performClick()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.third_dest_text)).assertIsDisplayed()

        // to Destination 4
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.third_dest_text)).performClick()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.fourth_dest_text)).assertIsDisplayed()

        // to Destination 1
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.fourth_dest_text)).performClick()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.first_dest_text)).assertIsDisplayed()
    }

    @Test
    fun app_dualPortrait_showsTwoPanes() {
        publisherRule.simulateVerticalFoldingFeature(composeTestRule)

        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.first_dest_text)).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.second_dest_text)).assertIsDisplayed()
    }

    @Test
    fun app_dualLandscape_showsTwoPanes() {
        publisherRule.simulateHorizontalFoldingFeature(composeTestRule)

        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.first_dest_text)).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.second_dest_text)).assertIsDisplayed()
    }
}
