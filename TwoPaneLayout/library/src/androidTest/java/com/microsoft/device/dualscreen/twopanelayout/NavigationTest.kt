package com.microsoft.device.dualscreen.twopanelayout

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.microsoft.device.dualscreen.testing.compose.foldableRuleChain
import com.microsoft.device.dualscreen.testing.filters.SingleScreenTest
import com.microsoft.device.dualscreen.testing.rules.FoldableTestRule
import com.microsoft.device.dualscreen.twopanelayout.twopanelayoutnav.TwoPaneNavScopeInstance.navigateTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class NavigationTest {
    private val composeTestRule = createAndroidComposeRule<TestActivity>()
    private val foldableTestRule = FoldableTestRule()

    @get:Rule
    val testRule: TestRule = foldableRuleChain(composeTestRule, foldableTestRule)

    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @Before
    fun setUp() {
        composeTestRule.setContent {
            val navController = rememberNavController()

            TwoPaneLayoutNav(
                navController = navController,
                singlePaneStartDestination = "A",
                pane1StartDestination = "A",
                pane2StartDestination = "B"
            ) {
                val destinations = listOf("A", "B", "C", "D", "E", "F")
                destinations.forEachIndexed { index, dest ->
                    composable(dest) {
                        val targetIndex = (index + 1).coerceAtMost(destinations.size - 1)
                        val targetRoute = destinations[targetIndex]
                        val launchScreen = if (index % 2 == 0) Screen.Pane1 else Screen.Pane2

                        Text(
                            modifier = Modifier.clickable { navController.navigateTo(targetRoute, launchScreen) },
                            text = dest
                        )
                    }
                }
            }
        }
    }

    @Test
    @SingleScreenTest
    fun navigateTo_singleScreen() {
        // Navigate through all destinations
        composeTestRule.onNodeWithText("A").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("B").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("C").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("D").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("E").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("F").assertIsDisplayed()
    }

    @Test
    @SingleScreenTest
    fun navigateUpTo_singleScreen() {
        // Navigate through all destinations
        composeTestRule.onNodeWithText("A").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("B").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("C").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("D").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("E").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("F").assertIsDisplayed()

        // Navigate back up through all destinations
        device.pressBack()
        composeTestRule.onNodeWithText("E").assertIsDisplayed()

        device.pressBack()
        composeTestRule.onNodeWithText("D").assertIsDisplayed()

        device.pressBack()
        composeTestRule.onNodeWithText("C").assertIsDisplayed()

        device.pressBack()
        composeTestRule.onNodeWithText("B").assertIsDisplayed()

        device.pressBack()
        composeTestRule.onNodeWithText("A").assertIsDisplayed()
    }
}
