package com.microsoft.device.dualscreen.twopanelayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
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
import com.microsoft.device.dualscreen.testing.createWindowLayoutInfoPublisherRule
import com.microsoft.device.dualscreen.twopanelayout.twopanelayoutnav.TwoPaneNavScopeInstance.navigateTo
import com.microsoft.device.dualscreen.twopanelayout.twopanelayoutnav.TwoPaneNavScopeInstance.navigateUpTo
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule

class NavigateUpToTest {
    private val publisherRule = createWindowLayoutInfoPublisherRule()
    private val composeTestRule = createAndroidComposeRule<NavigateUpToActivity>()

    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @get: Rule
    val testRule: TestRule

    init {
        testRule = RuleChain.outerRule(publisherRule).around(composeTestRule)
        RuleChain.outerRule(composeTestRule)
    }

    @Test
    fun navigateUpToInclusiveFalse_singleScreen() {
        composeTestRule.activity.setNavigateParams("B", false)

        // Navigate through all destinations
        composeTestRule.onNodeWithText("A").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("B").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("C").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("D").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("E").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("F").assertIsDisplayed()

        // Navigate back up through all destinations
        device.pressBack()
        composeTestRule.onNodeWithText("B").assertIsDisplayed()
    }

    @Test
    fun navigateUpToInclusiveTrue_singleScreen() {
        composeTestRule.activity.setNavigateParams("D", true)

        // Navigate through all destinations
        composeTestRule.onNodeWithText("A").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("B").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("C").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("D").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("E").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("F").assertIsDisplayed()

        // Navigate back up through all destinations
        device.pressBack()
        composeTestRule.onNodeWithText("C").assertIsDisplayed()
    }

    @Test
    fun navigateUpToInvalidRoute_singleScreen() {
        composeTestRule.activity.setNavigateParams("test", true)

        // Navigate through all destinations
        composeTestRule.onNodeWithText("A").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("B").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("C").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("D").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("E").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("F").assertIsDisplayed()

        // Navigate back up through all destinations
        device.pressBack()
        composeTestRule.onNodeWithText("F").assertIsDisplayed()
    }
}

class NavigateUpToActivity : ComponentActivity() {
    private var route: String = ""
    private var inclusive: Boolean = true

    fun setNavigateParams(route: String, inclusive: Boolean) {
        this.route = route
        this.inclusive = inclusive
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
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

                        BackHandler {
                            navController.navigateUpTo(this@NavigateUpToActivity.route, inclusive)
                        }

                        Text(
                            modifier = Modifier.clickable { navController.navigateTo(targetRoute, launchScreen) },
                            text = dest
                        )
                    }
                }
            }
        }
    }
}
