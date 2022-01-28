/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.composetesting

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule

/**
 * STRING HELPER
 * -----------------------------------------------------------------------------------------------
 * These functions can be used for string operations in UI tests to simplify testing code.
 */

/**
 * Get resource string inside Compose test with resource id
 *
 * @param id: string resource id
 */
fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.getString(@StringRes id: Int): String {
    return activity.getString(id)
}

/**
 * Get resource string inside Compose test with resource id and arguments
 *
 * @param id: string resource id
 * @param formatArgs: arguments to string
 */
fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.getString(@StringRes id: Int, vararg formatArgs: Any): String {
    return activity.getString(id, *formatArgs)
}
