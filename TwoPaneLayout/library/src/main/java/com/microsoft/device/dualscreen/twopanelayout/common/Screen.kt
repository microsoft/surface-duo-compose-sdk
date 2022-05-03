package com.microsoft.device.dualscreen.twopanelayout.common

/**
 * Class that represents the two possible screens in TwoPaneLayout (pane 1 and pane 2)
 */
sealed class Screen(val route: String) {
    override fun toString(): String {
        return route
    }

    /**
     * Screen object representing pane 1
     */
    object Pane1 : Screen("pane1")

    /**
     * Screen object representing pane 2
     */
    object Pane2 : Screen("pane2")
}