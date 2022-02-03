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
 * FOLD HELPER
 * -----------------------------------------------------------------------------------------------
 * These functions can be used in foldable UI tests to simulate the present of vertical and
 * horizontal folds/hinges. The folds are simulated using TestWindowLayoutInfo.
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
 * Simulate a vertical fold
 *
 * @param activityRule: test activity rule
 * @param center: location of center of fold
 * @param size: size of fold
 * @param state: state of fold
 */
fun <A : ComponentActivity> TestRule.simulateVerticalFold(
    activityRule: ActivityScenarioRule<A>,
    center: Int = -1,
    size: Int = 0,
    state: FoldingFeature.State = FoldingFeature.State.HALF_OPENED
) {
    simulateFold(activityRule, center, size, state, FoldingFeature.Orientation.VERTICAL)
}

/**
 * Simulate a horizontal fold
 *
 * @param activityRule: test activity rule
 * @param center: location of center of fold
 * @param size: size of fold
 * @param state: state of fold
 */
fun <A : ComponentActivity> TestRule.simulateHorizontalFold(
    activityRule: ActivityScenarioRule<A>,
    center: Int = -1,
    size: Int = 0,
    state: FoldingFeature.State = FoldingFeature.State.HALF_OPENED
) {
    simulateFold(activityRule, center, size, state, FoldingFeature.Orientation.HORIZONTAL)
}

/**
 * Simulate a fold with the given properties
 *
 * @param activityRule: test activity rule
 * @param center: location of center of fold
 * @param size: size of fold
 * @param state: state of fold
 * @param orientation: orientation of fold
 */
fun <A : ComponentActivity> TestRule.simulateFold(
    activityRule: ActivityScenarioRule<A>,
    center: Int,
    size: Int,
    state: FoldingFeature.State,
    orientation: FoldingFeature.Orientation,
) {
    this as? WindowLayoutInfoPublisherRule
        ?: throw ClassCastException("Test rule is not of type WindowLayoutInfoPublisherRule")

    activityRule.scenario.onActivity { activity ->
        val fold = FoldingFeature(
            activity = activity,
            center = center,
            size = size,
            state = state,
            orientation = orientation
        )
        val windowLayoutInfo = TestWindowLayoutInfo(listOf(fold))
        overrideWindowLayoutInfo(windowLayoutInfo)
    }
}

/**
 * Simulate a vertical fold in a Compose test
 *
 * @param composeTestRule: Compose android test rule
 * @param center: location of center of fold
 * @param size: size of fold
 * @param state: state of fold
 */
fun <A : ComponentActivity> TestRule.simulateVerticalFold(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<A>, A>,
    center: Int = -1,
    size: Int = 0,
    state: FoldingFeature.State = FoldingFeature.State.HALF_OPENED
) {
    simulateVerticalFold(composeTestRule.activityRule, center, size, state)
}

/**
 * Simulate a horizontal fold in a Compose test
 *
 * @param composeTestRule: Compose android test rule
 * @param center: location of center of fold
 * @param size: size of fold
 * @param state: state of fold
 */
fun <A : ComponentActivity> TestRule.simulateHorizontalFold(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<A>, A>,
    center: Int = -1,
    size: Int = 0,
    state: FoldingFeature.State = FoldingFeature.State.HALF_OPENED
) {
    simulateHorizontalFold(composeTestRule.activityRule, center, size, state)
}

/**
 * Simulate a fold with the given properties in a Compose test
 *
 * @param composeTestRule: Compose android test rule
 * @param center: location of center of fold
 * @param size: size of fold
 * @param state: state of fold
 * @param orientation: orientation of fold
 */
fun <A : ComponentActivity> TestRule.simulateFold(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<A>, A>,
    center: Int,
    size: Int,
    state: FoldingFeature.State,
    orientation: FoldingFeature.Orientation,
) {
    simulateFold(composeTestRule.activityRule, center, size, state, orientation)
}
