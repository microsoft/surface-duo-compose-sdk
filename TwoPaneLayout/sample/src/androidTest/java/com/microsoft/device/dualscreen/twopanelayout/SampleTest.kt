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
import com.microsoft.device.dualscreen.twopanelayout.twopanelayout.TestTwoPaneScopeInstance
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

    private fun setUpMainPage() {
        composeTestRule.setContent {
            MainPage()
        }
    }

    @Test
    fun app_launches() {
        setUpMainPage()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.app_name)).assertIsDisplayed()
    }

    @Test
    fun app_canNavigateToSecondPane() {
        setUpMainPage()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.first_pane_text)).performClick()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.second_pane_text)).assertIsDisplayed()
    }

    @Test
    fun app_canNavigateToFirstPane() {
        setUpMainPage()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.first_pane_text)).performClick()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.second_pane_text)).performClick()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.first_pane_text)).assertIsDisplayed()
    }

    @Test
    fun app_dualPortrait_showsTwoPanes() {
        setUpMainPage()
        publisherRule.simulateVerticalFoldingFeature(composeTestRule)

        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.first_pane_text)).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.second_pane_text)).assertIsDisplayed()
    }

    @Test
    fun app_dualLandscape_showsOnePane() {
        setUpMainPage()
        publisherRule.simulateHorizontalFoldingFeature(composeTestRule)

        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.first_pane_text)).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.second_pane_text)).assertDoesNotExist()
    }

    @Test
    fun topBar_singlePane_showsCorrectPaneString() {
        composeTestRule.setContent {
            TestTwoPaneScopeInstance.setIsSinglePane(true)
            TestTwoPaneScopeInstance.TopAppBar(pane = R.string.pane1)
        }

        composeTestRule.onNodeWithText(
            composeTestRule.getString(R.string.app_name) + " " + composeTestRule.getString(R.string.pane1)
        ).assertDoesNotExist()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.app_name)).assertIsDisplayed()
    }

    @Test
    fun topBar_twoPanes_showsCorrectPaneString() {
        composeTestRule.setContent {
            TestTwoPaneScopeInstance.setIsSinglePane(false)
            TestTwoPaneScopeInstance.TopAppBar(pane = R.string.pane1)
        }

        composeTestRule.onNodeWithText(
            composeTestRule.getString(R.string.app_name) + " " + composeTestRule.getString(R.string.pane1)
        ).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.app_name)).assertDoesNotExist()
    }
}
