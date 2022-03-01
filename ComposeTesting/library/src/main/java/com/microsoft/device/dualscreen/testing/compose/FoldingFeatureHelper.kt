/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.testing.compose

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.window.layout.FoldingFeature
import com.microsoft.device.dualscreen.testing.simulateHorizontalFoldingFeature
import com.microsoft.device.dualscreen.testing.simulateVerticalFoldingFeature
import org.junit.rules.TestRule

/**
 * COMPOSE FOLDINGFEATURE HELPER
 * -----------------------------------------------------------------------------------------------
 * These functions can be used in Compose foldable UI tests to simulate the present of vertical and
 * horizontal foldingFeatures(folds/hinges). The foldingFeatures are simulated using TestWindowLayoutInfo.
 */

/**
 * Simulate a vertical foldingFeature in a Compose test
 *
 * @param composeTestRule: Compose android test rule
 * @param center: location of center of foldingFeature
 * @param size: size of foldingFeature
 * @param state: state of foldingFeature
 */
fun <A : ComponentActivity> TestRule.simulateVerticalFoldingFeature(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<A>, A>,
    center: Int = -1,
    size: Int = 0,
    state: FoldingFeature.State = FoldingFeature.State.HALF_OPENED
) {
    simulateVerticalFoldingFeature(composeTestRule.activityRule, center, size, state)
}

/**
 * Simulate a horizontal foldingFeature in a Compose test
 *
 * @param composeTestRule: Compose android test rule
 * @param center: location of center of foldingFeature
 * @param size: size of foldingFeature
 * @param state: state of foldingFeature
 */
fun <A : ComponentActivity> TestRule.simulateHorizontalFoldingFeature(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<A>, A>,
    center: Int = -1,
    size: Int = 0,
    state: FoldingFeature.State = FoldingFeature.State.HALF_OPENED
) {
    simulateHorizontalFoldingFeature(composeTestRule.activityRule, center, size, state)
}
