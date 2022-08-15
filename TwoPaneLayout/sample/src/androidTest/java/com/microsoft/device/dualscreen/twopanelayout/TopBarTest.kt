package com.microsoft.device.dualscreen.twopanelayout

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.microsoft.device.dualscreen.testing.compose.getString
import com.microsoft.device.dualscreen.twopanelayout.twopanelayout.TwoPaneScopeTest
import org.junit.Rule
import org.junit.Test

class TopBarTest {
    @get: Rule
    val composeTestRule = createComposeRule()

    @Test
    fun topBar_singlePane_showsCorrectPaneString() {
        composeTestRule.setContent {
            val twoPaneScopeTest = TwoPaneScopeTest(isSinglePane = true)
            twoPaneScopeTest.TopAppBar(pane = R.string.pane1)
        }

        composeTestRule.onNodeWithText(
            getString(R.string.app_name) + " " + getString(R.string.pane1)
        ).assertDoesNotExist()
        composeTestRule.onNodeWithText(getString(R.string.app_name)).assertIsDisplayed()
    }

    @Test
    fun topBar_twoPanes_showsCorrectPaneString() {
        composeTestRule.setContent {
            val twoPaneScopeTest = TwoPaneScopeTest(isSinglePane = false)
            twoPaneScopeTest.TopAppBar(pane = R.string.pane1)
        }

        composeTestRule.onNodeWithText(
            getString(R.string.app_name) + " " + getString(R.string.pane1)
        ).assertIsDisplayed()
        composeTestRule.onNodeWithText(getString(R.string.app_name)).assertDoesNotExist()
    }
}
