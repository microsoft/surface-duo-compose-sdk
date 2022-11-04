package com.microsoft.device.dualscreen.twopanelayout.twopanelayoutnav

import com.microsoft.device.dualscreen.twopanelayout.Screen

/**
 * Class that represents an entry in the manual TwoPaneLayoutNav backstack
 *
 * @param route: route of the destination in the entry
 * @param launchScreen: screen in which the entry was shown in two pane mode
 */
internal data class TwoPaneBackStackEntry(val route: String, val launchScreen: Screen)

internal fun MutableList<TwoPaneBackStackEntry>.initialize(
    startDestination1: String,
    startDestination2: String? = null
) {
    check(this.isEmpty()) { "Attempting to initialize non-empty back stack" }

    // REVISIT: assumes that single pane start destination should be assigned to pane 1
    add(TwoPaneBackStackEntry(startDestination1, Screen.Pane1))
    startDestination2?.let { add(TwoPaneBackStackEntry(startDestination2, Screen.Pane2)) }
}
