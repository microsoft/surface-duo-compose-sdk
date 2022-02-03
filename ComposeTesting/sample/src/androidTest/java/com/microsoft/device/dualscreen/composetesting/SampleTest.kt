/*
 *
 *  * Copyright (c) Microsoft Corporation. All rights reserved.
 *  * Licensed under the MIT License.
 *
 */

package com.microsoft.device.dualscreen.composetesting

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.microsoft.device.dualscreen.composetesting.ui.theme.ComposeTestingTheme
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule

class SampleTest {
    private val composeTestRule = createAndroidComposeRule<MainActivity>()
    private val publisherRule = createWindowLayoutInfoPublisherRule()

    @get: Rule
    val testRule: TestRule

    init {
        testRule = RuleChain.outerRule(publisherRule).around(composeTestRule)
        RuleChain.outerRule(composeTestRule)
    }

    @Test
    fun app_deviceOpenVertically_showTwoPanes() {
        composeTestRule.setContent {
            ComposeTestingTheme {
                ComposeTestingApp()
            }
        }
        // Simulate vertical foldingFeature
        publisherRule.simulateVerticalFoldingFeature(composeTestRule)
    }
}
