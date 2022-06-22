/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.navigationrail

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.semantics.SemanticsProperties.VerticalScrollAxisRange
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.window.testing.layout.WindowLayoutInfoPublisherRule
import com.microsoft.device.display.samples.navigationrail.models.DataProvider
import com.microsoft.device.display.samples.navigationrail.ui.theme.NavigationRailAppTheme
import com.microsoft.device.display.samples.navigationrail.ui.view.GallerySections
import com.microsoft.device.display.samples.navigationrail.ui.view.NavigationRailApp
import com.microsoft.device.dualscreen.testing.compose.getString
import com.microsoft.device.dualscreen.testing.compose.simulateVerticalFoldingFeature
import com.microsoft.device.dualscreen.windowstate.WindowState
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule

@ExperimentalFoundationApi
@ExperimentalUnitApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
class PaneSynchronizationTest {
    private val composeTestRule = createAndroidComposeRule<MainActivity>()
    private val publisherRule = WindowLayoutInfoPublisherRule()

    @get: Rule
    val testRule: TestRule

    init {
        testRule = RuleChain.outerRule(publisherRule).around(composeTestRule)
        RuleChain.outerRule(composeTestRule)
    }

    /**
     * Tests that the correct placeholder views appear when a gallery is first opened and no items are selected
     */
    @Test
    fun app_verticalFold_testPlaceholderViewAppearsOnStart() {
        composeTestRule.setContent {
            NavigationRailAppTheme {
                NavigationRailApp(WindowState(hasFold = true, foldIsSeparating = true))
            }
        }

        // Simulate a vertical foldFeature
        publisherRule.simulateVerticalFoldingFeature(composeTestRule)

        for (gallery in GallerySections.values()) {
            // Click on gallery
            composeTestRule.onNode(hasText(gallery.route, ignoreCase = true) and hasClickAction()).performClick()

            // Assert that correct placeholder view is being shown
            val selectText = composeTestRule.activity.getString(R.string.placeholder_msg, gallery.route)
            composeTestRule.onNodeWithText(selectText).assertIsDisplayed()
        }
    }

    /**
     * Tests that clicking all of the various gallery items updates the detail pane to the correct detail view
     * for the clicked item when a vertical fold is present
     */
    @Test
    fun app_verticalFold_galleryClickUpdatesSelection() {
        composeTestRule.setContent {
            NavigationRailAppTheme {
                NavigationRailApp(WindowState(hasFold = true, foldIsSeparating = true))
            }
        }

        // Simulate a vertical foldFeature
        publisherRule.simulateVerticalFoldingFeature(composeTestRule)

        GallerySections.values().forEachIndexed { i, gallery ->
            // Click on gallery
            composeTestRule.onNode(hasText(gallery.route, ignoreCase = true) and hasClickAction())
                .performClick()

            // Get number of visible children for lazy grid
            val gridSize = composeTestRule.onNode(SemanticsMatcher.keyIsDefined(VerticalScrollAxisRange))
                .fetchSemanticsNode().children.size

            // Go through entries for current gallery (each gallery contains 12 entries)
            DataProvider.images.subList(i * 12, (i + 1) * 12).forEachIndexed { index, entry ->
                // Get content description for current entry
                val contentDescription = composeTestRule.getString(R.string.image_description, entry.name, entry.id)

                // If we've reached the end of a "page" in the gallery (gone through all of the visible children),
                // perform a swipe up gesture to make sure the next "page" of entries is visible
                if (index != 0 && index % gridSize == 0) {
                    composeTestRule.onNode(
                        hasScrollAction() and
                            hasAnyChild(hasContentDescription(contentDescription))
                    ).performTouchInput { swipeUp() }
                }

                // Scroll to the current entry and click on it
                composeTestRule.onNodeWithContentDescription(contentDescription).performScrollTo().performClick()

                // Assert correct details are shown in second pane
                composeTestRule.onNodeWithText(entry.name).assertIsDisplayed()
                composeTestRule.onNodeWithText(entry.location).assertIsDisplayed()
                composeTestRule.onNodeWithText(entry.fact1).assertIsDisplayed()
            }
        }
    }
}
