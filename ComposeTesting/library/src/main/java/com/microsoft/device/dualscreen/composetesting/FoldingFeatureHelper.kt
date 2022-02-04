/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.composetesting

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.window.layout.FoldingFeature
import androidx.window.testing.layout.FoldingFeature
import androidx.window.testing.layout.TestWindowLayoutInfo
import androidx.window.testing.layout.WindowLayoutInfoPublisherRule
import org.junit.rules.TestRule
import java.lang.ClassCastException

/**
 * FOLDINGFEATURE HELPER
 * -----------------------------------------------------------------------------------------------
 * These functions can be used in foldable UI tests to simulate the present of vertical and
 * horizontal foldingFeatures(folds/hinges). The foldingFeatures are simulated using TestWindowLayoutInfo.
 */

/**
 * Return WindowLayoutInfoPublisherRule which allows you to push through different WindowLayoutInfo
 * values on demand from Window.testing to test
 *
 */
fun createWindowLayoutInfoPublisherRule(): TestRule {
    return WindowLayoutInfoPublisherRule()
}

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

/**
 * Simulate a foldingFeature with the given properties in a Compose test
 *
 * @param composeTestRule: Compose android test rule
 * @param center: location of center of foldingFeature
 * @param size: size of foldingFeature
 * @param state: state of foldingFeature
 * @param orientation: orientation of foldingFeature
 */
fun <A : ComponentActivity> TestRule.simulateFoldingFeature(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<A>, A>,
    center: Int,
    size: Int,
    state: FoldingFeature.State,
    orientation: FoldingFeature.Orientation,
) {
    simulateFoldingFeature(composeTestRule.activityRule, center, size, state, orientation)
}

/**
 * Simulate a vertical foldingFeature
 *
 * @param activityRule: test activity rule
 * @param center: location of center of foldingFeature
 * @param size: size of foldingFeature
 * @param state: state of foldingFeature
 */
private fun <A : ComponentActivity> TestRule.simulateVerticalFoldingFeature(
    activityRule: ActivityScenarioRule<A>,
    center: Int = -1,
    size: Int = 0,
    state: FoldingFeature.State = FoldingFeature.State.HALF_OPENED
) {
    simulateFoldingFeature(activityRule, center, size, state, FoldingFeature.Orientation.VERTICAL)
}

/**
 * Simulate a horizontal foldingFeature
 *
 * @param activityRule: test activity rule
 * @param center: location of center of foldingFeature
 * @param size: size of foldingFeature
 * @param state: state of foldingFeature
 */
private fun <A : ComponentActivity> TestRule.simulateHorizontalFoldingFeature(
    activityRule: ActivityScenarioRule<A>,
    center: Int = -1,
    size: Int = 0,
    state: FoldingFeature.State = FoldingFeature.State.HALF_OPENED
) {
    simulateFoldingFeature(activityRule, center, size, state, FoldingFeature.Orientation.HORIZONTAL)
}

/**
 * Simulate a foldingFeature with the given properties
 *
 * @param activityRule: test activity rule
 * @param center: location of center of foldingFeature
 * @param size: size of foldingFeature
 * @param state: state of foldingFeature
 * @param orientation: orientation of foldingFeature
 */
private fun <A : ComponentActivity> TestRule.simulateFoldingFeature(
    activityRule: ActivityScenarioRule<A>,
    center: Int,
    size: Int,
    state: FoldingFeature.State,
    orientation: FoldingFeature.Orientation,
) {
    this as? WindowLayoutInfoPublisherRule
        ?: throw ClassCastException("Test rule is not of type WindowLayoutInfoPublisherRule")

    activityRule.scenario.onActivity { activity ->
        val foldingFeature = FoldingFeature(
            activity = activity,
            center = center,
            state = state,
            size = size,
            orientation = orientation
        )
        val windowLayoutInfo = TestWindowLayoutInfo(listOf(foldingFeature))
        overrideWindowLayoutInfo(windowLayoutInfo)
    }
}
