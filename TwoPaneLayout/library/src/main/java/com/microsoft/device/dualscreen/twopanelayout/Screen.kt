package com.microsoft.device.dualscreen.twopanelayout

/**
 * Class that represents the screens in the NavHost
 */
sealed class Screen(val route: String) {
    /**
     * Screen object representing pane 1
     */
    object Pane1 : Screen("pane1")

    /**
     * Screen object representing pane 2
     */
    object Pane2 : Screen("pane2")
}
