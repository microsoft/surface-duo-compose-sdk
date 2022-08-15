package com.microsoft.device.dualscreen.twopanelayout

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.microsoft.device.dualscreen.testing.compose.getString
import com.microsoft.device.dualscreen.twopanelayout.twopanelayoutnav.TwoPaneNavScopeTest
import org.junit.Rule
import org.junit.Test

class BasicDestinationTest {
    @get: Rule
    val composeTestRule = createComposeRule()

    @Test
    fun basicDestination_singlePane_showsCorrectPaneString() {
        composeTestRule.setContent {
            val navController = rememberNavController()

            val twoPaneNavScopeTest = TwoPaneNavScopeTest(isSinglePane = true)
            twoPaneNavScopeTest.BasicDestination(
                navController = navController,
                sampleDestination = SampleDestination.DEST4,
            )
        }

        composeTestRule.onNodeWithText(
            getString(R.string.app_name) + " " + getString(R.string.pane1)
        ).assertDoesNotExist()
        composeTestRule.onNodeWithText(getString(R.string.app_name)).assertIsDisplayed()
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
            getString(R.string.app_name) + " " + getString(R.string.pane1)
        ).assertIsDisplayed()
        composeTestRule.onNodeWithText(getString(R.string.app_name)).assertDoesNotExist()
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

        composeTestRule.onNodeWithText(getString(R.string.fourth_dest_text)).assertIsDisplayed()
    }
}
