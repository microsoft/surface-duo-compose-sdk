/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.navigationrail

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import com.microsoft.device.display.samples.navigationrail.ui.theme.NavigationRailAppTheme
import com.microsoft.device.display.samples.navigationrail.ui.view.GallerySections
import com.microsoft.device.display.samples.navigationrail.ui.view.NavigationRailApp
import com.microsoft.device.dualscreen.testing.compose.getString
import com.microsoft.device.dualscreen.windowstate.WindowState
import org.junit.Rule
import org.junit.Test

@ExperimentalFoundationApi
@ExperimentalUnitApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
class NavComponentTest {

    @get: Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    /**
     * Tests that clicking each icon in the bottom navigation bar switches the gallery destination
     */
    @Test
    fun app_bottomNav_testButtonClickSwitchesDestination() {
        composeTestRule.setContent {
            NavigationRailAppTheme {
                NavigationRailApp(WindowState())
            }
        }

        clickEachNavIcon()
    }

    /**
     * Tests that clicking each icon in the navigation rail switches the gallery destination
     */
    @Test
    fun app_navRail_testButtonClickSwitchesDestination() {
        composeTestRule.setContent {
            NavigationRailAppTheme {
                NavigationRailApp(WindowState(hasFold = true, foldIsSeparating = true))
            }
        }

        clickEachNavIcon()
    }

    /**
     * Tests that a navigation rail component is used in the app when a vertical fold is present
     */
    @Test
    fun app_verticalFold_navRailIsShown() {
        composeTestRule.setContent {
            NavigationRailAppTheme {
                NavigationRailApp(WindowState(hasFold = true, foldIsSeparating = true))
            }
        }

        // Assert that nav rail, not bottom nav, is displayed
        composeTestRule.onNodeWithTag(composeTestRule.getString(R.string.nav_rail)).assertIsDisplayed()
        composeTestRule.onNodeWithTag(composeTestRule.getString(R.string.bottom_nav)).assertDoesNotExist()
    }

    /**
     * Tests that a navigation rail component is used in the app when a horizontal fold is present
     */
    @Test
    fun app_horizontalFold_navRailIsShown() {
        composeTestRule.setContent {
            NavigationRailAppTheme {
                NavigationRailApp(WindowState(hasFold = true, foldIsHorizontal = true, foldIsSeparating = true))
            }
        }

        // Assert that nav rail, not bottom nav, is displayed
        composeTestRule.onNodeWithTag(composeTestRule.getString(R.string.nav_rail)).assertIsDisplayed()
        composeTestRule.onNodeWithTag(composeTestRule.getString(R.string.bottom_nav)).assertDoesNotExist()
    }

    /**
     * Tests that a navigation rail component is used when the app is displayed on a large screen
     */
    @Test
    fun app_largeScreen_navRailIsShown() {
        composeTestRule.setContent {
            NavigationRailAppTheme {
                NavigationRailApp(WindowState(windowWidthDp = 1000.dp))
            }
        }

        // Assert that nav rail, not bottom nav, is displayed
        composeTestRule.onNodeWithTag(composeTestRule.getString(R.string.nav_rail)).assertIsDisplayed()
        composeTestRule.onNodeWithTag(composeTestRule.getString(R.string.bottom_nav)).assertDoesNotExist()
    }

    /**
     * Tests that a bottom navigation component is used in the app when in single screen mode
     */
    @Test
    fun app_singleScreen_bottomNavIsShown() {
        composeTestRule.setContent {
            NavigationRailAppTheme {
                NavigationRailApp(WindowState())
            }
        }

        // Assert that bottom nav, not nav rail, is displayed
        composeTestRule.onNodeWithTag(composeTestRule.getString(R.string.bottom_nav)).assertIsDisplayed()
        composeTestRule.onNodeWithTag(composeTestRule.getString(R.string.nav_rail)).assertDoesNotExist()
    }

    /**
     * Helper function that clicks on each nav icon in the current nav component and then asserts that the
     * destination was properly updated
     */
    private fun clickEachNavIcon() {
        for (destination in GallerySections.values()) {
            // Click on nav icon
            composeTestRule.onNode(hasText(composeTestRule.getString(destination.title)) and hasClickAction())
                .performClick()

            // Assert that new destination route is shown in the top bar
            composeTestRule.onNode(hasText(destination.route)).assertIsDisplayed()
        }
    }
}
