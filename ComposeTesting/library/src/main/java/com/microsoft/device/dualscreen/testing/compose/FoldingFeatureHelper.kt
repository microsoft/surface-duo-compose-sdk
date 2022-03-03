/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.testing.compose

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.window.layout.FoldingFeature
import com.microsoft.device.dualscreen.testing.simulateFlipDevice
import com.microsoft.device.dualscreen.testing.simulateFoldDevice
import com.microsoft.device.dualscreen.testing.simulateHorizontalFoldingFeature
import com.microsoft.device.dualscreen.testing.simulateSurfaceDuo1
import com.microsoft.device.dualscreen.testing.simulateSurfaceDuo2
import com.microsoft.device.dualscreen.testing.simulateVerticalFoldingFeature
import org.junit.rules.TestRule

/**
 * COMPOSE FOLDINGFEATURE HELPER
 * -----------------------------------------------------------------------------------------------
 * These functions can be used in Compose foldable UI tests to simulate the present of vertical and
 * horizontal folding features (folds/hinges). The folding features are simulated using TestWindowLayoutInfo from
 * Google's Jetpack Window Manager testing library.
 */

/**
 * Simulate a vertical folding feature in a Compose test
 *
 * @param composeTestRule: Compose android test rule
 * @param center: location of center of folding feature
 * @param size: size of folding feature
 * @param state: state of folding feature
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
 * Simulate a horizontal folding feature in a Compose test
 *
 * @param composeTestRule: Compose android test rule
 * @param center: location of center of folding feature
 * @param size: size of folding feature
 * @param state: state of folding feature
 */
fun <A : ComponentActivity> TestRule.simulateHorizontalFoldingFeature(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<A>, A>,
    center: Int = -1,
    size: Int = 0,
    state: FoldingFeature.State = FoldingFeature.State.HALF_OPENED
) {
    simulateHorizontalFoldingFeature(composeTestRule.activityRule, center, size, state)
}

/**
 * Simulate a folding feature with the dimensions from the Surface Duo 1 (centered, 84px hinge)
 *
 * @param composeTestRule: Compose android test rule
 * @param state: state of folding feature
 * @param orientation: orientation of folding feature
 */
fun <A : ComponentActivity> TestRule.simulateSurfaceDuo1(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<A>, A>,
    state: FoldingFeature.State = FoldingFeature.State.FLAT,
    orientation: FoldingFeature.Orientation = FoldingFeature.Orientation.VERTICAL
) {
    simulateSurfaceDuo1(composeTestRule.activityRule, state, orientation)
}

/**
 * Simulate a folding feature with the dimensions from the Surface Duo 2 (centered, 66px hinge)
 *
 * @param composeTestRule: Compose android test rule
 * @param state: state of folding feature
 * @param orientation: orientation of folding feature
 */
fun <A : ComponentActivity> TestRule.simulateSurfaceDuo2(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<A>, A>,
    state: FoldingFeature.State = FoldingFeature.State.FLAT,
    orientation: FoldingFeature.Orientation = FoldingFeature.Orientation.VERTICAL
) {
    simulateSurfaceDuo2(composeTestRule.activityRule, state, orientation)
}

/**
 * Simulate a folding feature with the dimensions from a Fold device (centered, vertical, 0px fold)
 *
 * @param composeTestRule: Compose android test rule
 * @param state: state of folding feature
 */
fun <A : ComponentActivity> TestRule.simulateFoldDevice(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<A>, A>,
    state: FoldingFeature.State = FoldingFeature.State.HALF_OPENED
) {
    simulateFoldDevice(composeTestRule.activityRule, state)
}

/**
 * Simulate a folding feature with the dimensions from a Flip device (centered, horizontal, 0px fold)
 *
 * @param composeTestRule: Compose android test rule
 * @param state: state of folding feature
 */
fun <A : ComponentActivity> TestRule.simulateFlipDevice(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<A>, A>,
    state: FoldingFeature.State = FoldingFeature.State.HALF_OPENED
) {
    simulateFlipDevice(composeTestRule.activityRule, state)
}
