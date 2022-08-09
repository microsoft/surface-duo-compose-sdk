/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.testing.compose

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry

/**
 * COMPOSE STRING HELPER
 * -----------------------------------------------------------------------------------------------
 * These functions can be used for string operations in UI tests to simplify testing code.
 */

/**
 * Get string resource inside Compose test with resource id
 *
 * @param id: string resource id
 */
fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.getString(@StringRes id: Int): String {
    return activity.getString(id)
}

/**
 * Get string resource inside Compose test with resource id and arguments
 *
 * @param id: string resource id
 * @param formatArgs: arguments to string
 */
fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.getString(
    @StringRes id: Int,
    vararg formatArgs: Any
): String {
    return activity.getString(id, *formatArgs)
}

/**
 * Get string resource in an instrumented test when no activity is available through a test rule
 *
 * @param id: string resource id
 */
fun getString(@StringRes id: Int): String {
    return InstrumentationRegistry.getInstrumentation().targetContext.resources.getString(id)
}

/**
 * Get string resource with arguments in an instrumented test when no activity is available through a test rule
 *
 * @param id: string resource id
 * @param formatArgs: arguments to string
 */
fun getString(@StringRes id: Int, vararg formatArgs: Any): String {
    return InstrumentationRegistry.getInstrumentation().targetContext.resources.getString(id, formatArgs)
}
