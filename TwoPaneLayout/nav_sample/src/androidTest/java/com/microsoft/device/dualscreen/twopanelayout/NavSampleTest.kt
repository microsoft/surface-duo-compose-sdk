/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.twopanelayout

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.microsoft.device.dualscreen.testing.compose.getString
import com.microsoft.device.dualscreen.testing.compose.simulateHorizontalFoldingFeature
import com.microsoft.device.dualscreen.testing.compose.simulateVerticalFoldingFeature
import com.microsoft.device.dualscreen.testing.createWindowLayoutInfoPublisherRule
import com.microsoft.device.dualscreen.twopanelayout.twopanelayoutnav.TwoPaneNavScopeTest
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
    fun app_canNavigateToAllDestinations() {
        setUpMainPage()

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
        setUpMainPage()
        publisherRule.simulateVerticalFoldingFeature(composeTestRule)

        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.first_dest_text)).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.second_dest_text)).assertIsDisplayed()
    }

    @Test
    fun app_dualLandscape_showsTwoPanes() {
        setUpMainPage()
        publisherRule.simulateHorizontalFoldingFeature(composeTestRule)

        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.first_dest_text)).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.second_dest_text)).assertIsDisplayed()
    }

    @Test
    fun basicDestination_singlePane_showsCorrectPaneString() {
        composeTestRule.setContent {
            val navController = rememberNavController()

            val twoPaneNavScopeTest = TwoPaneNavScopeTest(isSinglePane = false)
            twoPaneNavScopeTest.BasicDestination(
                navController = navController,
                sampleDestination = SampleDestination.DEST4,
            )
        }

        composeTestRule.onNodeWithText(
            composeTestRule.getString(R.string.app_name) + " " + composeTestRule.getString(R.string.pane1)
        ).assertDoesNotExist()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.app_name)).assertIsDisplayed()
    }

    @Test
    fun basicDestination_twoPanes_showsCorrectPaneString() {
        composeTestRule.setContent {
            val navController = rememberNavController()

            val twoPaneNavScopeTest = TwoPaneNavScopeTest(
                isSinglePane = false,
                currentPane1Destination = SampleDestination.DEST4.route
            )
            twoPaneNavScopeTest.BasicDestination(
                navController = navController,
                sampleDestination = SampleDestination.DEST4,
            )
        }

        composeTestRule.onNodeWithText(
            composeTestRule.getString(R.string.app_name) + " " + composeTestRule.getString(R.string.pane1)
        ).assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.app_name)).assertDoesNotExist()
    }

    @Test
    fun basicDestination_showsCorrectText() {
        composeTestRule.setContent {
            val navController = rememberNavController()

            val twoPaneNavScopeTest = TwoPaneNavScopeTest()
            twoPaneNavScopeTest.BasicDestination(
                navController = navController,
                sampleDestination = SampleDestination.DEST4,
            )
        }

        composeTestRule.onNodeWithText(composeTestRule.getString(R.string.fourth_dest_text)).assertIsDisplayed()
    }
}
