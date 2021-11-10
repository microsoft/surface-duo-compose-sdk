/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.twopanelayout

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SampleTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            MainPage()
        }
    }

    private val appName = "TwoPaneLayoutSample"
    private val firstPaneText = "First pane: TwoPaneLayout is a UI component for Jetpack Compose, which contains the layouts that help you create UI for dual-screen, foldable, and large-screen devices. TwoPaneLayout provides a two-pane layout for use at the top level of a UI. The component will place two panes side-by-side on dual-screen, foldable, and large-screen devices and one pane only on regular single-screen devices. The two panes can be horizontal or vertical, based on the orientation of the device, unless paneMode is configured."
    private val secondPaneText = "Second pane: The element layout is based on the order, which means the first element will be placed in the first pane and the second element will be placed in the second pane. The TwoPaneLayout is able to assign children widths according to their weights provided using the TwoPaneScope.weight modifier. When none of its children have weights, the first child element will be prioritized when the app is running on a regular single-screen device, or when foldable and dual-screen devices become folded or unspanned. If the app is spanned or unfolded on foldable and dual-screen, or large-screen devices, the two elements will be laid out equally to take all the display area."

    @Test
    fun app_launches() {
        composeTestRule.onNodeWithText(appName).assertIsDisplayed()
    }

    @Test
    fun app_canNavigateToSecondPane() {
        composeTestRule.onNodeWithText(firstPaneText).performClick()
        composeTestRule.onNodeWithText(secondPaneText).assertIsDisplayed()
    }

    @Test
    fun app_canNavigateToFirstPane() {
        composeTestRule.onNodeWithText(firstPaneText).performClick()
        composeTestRule.onNodeWithText(secondPaneText).performClick()
        composeTestRule.onNodeWithText(firstPaneText).assertIsDisplayed()
    }
}
