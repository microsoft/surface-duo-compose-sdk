/*
 *
 *  * Copyright (c) Microsoft Corporation. All rights reserved.
 *  * Licensed under the MIT License.
 *
 */

package com.microsoft.device.dualscreen.testing.compose

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.microsoft.device.dualscreen.testing.isSurfaceDuo
import com.microsoft.device.dualscreen.testing.rules.FoldableTestRule
import org.junit.rules.RuleChain
import org.junit.rules.TestRule

fun <A : ComponentActivity> foldableRuleChain(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<A>, A>,
    foldableTestRule: FoldableTestRule,
    vararg aroundRules: TestRule
): RuleChain {
    val uiDevice: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    var ruleChain = if (uiDevice.isSurfaceDuo()) {
        RuleChain.outerRule(composeTestRule).around(foldableTestRule)
    } else {
        RuleChain.outerRule(foldableTestRule).around(composeTestRule)
    }

    aroundRules.forEach {
        ruleChain = ruleChain.around(it)
    }
    return ruleChain
}
