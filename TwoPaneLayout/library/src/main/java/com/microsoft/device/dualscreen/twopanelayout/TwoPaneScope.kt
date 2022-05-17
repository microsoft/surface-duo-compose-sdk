package com.microsoft.device.dualscreen.twopanelayout

import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder

@LayoutScopeMarker
@Immutable
interface TwoPaneScope {
    /**
     * Determines how much of the screen content will occupy
     *
     * @param weight: percentage of the screen to fill (between 0-1)
     */
    @Stable
    fun Modifier.weight(weight: Float): Modifier

    /**
     * Navigates to the content in the first pane in single pane mode, otherwise does nothing in two pane mode
     */
    @Stable
    fun navigateToPane1()

    /**
     * Navigates to the content in the second pane in single pane mode, otherwise does nothing in two pane mode
     */
    @Stable
    fun navigateToPane2()

    /**
     * The route of the destination currently shown in the single pane layout (either [Screen.Pane1.route] or
     * [Screen.Pane2.route])
     */
    @Stable
    val currentSinglePaneDestination: String

    /**
     * Returns true when in single pane mode, false when in two pane mode
     */
    @Stable
    val isSinglePane: Boolean
}

@LayoutScopeMarker
@Immutable
interface TwoPaneNavScope {
    /**
     * Determines how much of the screen content will occupy
     *
     * @param weight: percentage of the screen to fill (between 0-1)
     */
    @Stable
    fun Modifier.weight(weight: Float): Modifier

    /**
     * Navigates to the given destination. In single pane mode, this changes the current destination
     * in the NavHost. In two pane mode, this updates the content in the given screen/pane.
     *
     * @param route: route of the destination to navigate to
     * @param screen: the screen (pane 1 or pane 2) in which to change content in two pane mode
     * @param navOptions: optional navigation options to use in single pane mode
     */
    @Stable
    fun NavHostController.navigateTo(
        route: String,
        screen: Screen,
        navOptions: NavOptionsBuilder.() -> Unit = { },
    )

    /**
     * The route of the destination currently shown in the single pane layout
     */
    @Stable
    val currentSinglePaneDestination: String

    /**
     * The route of the destination currently shown in pane 1 of the two pane layout
     */
    @Stable
    val currentPane1Destination: String

    /**
     * The route of the destination currently shown in pane 2 of the two pane layout
     */
    @Stable
    val currentPane2Destination: String

    /**
     * Returns true when in single pane mode, false when in two pane mode
     */
    @Stable
    val isSinglePane: Boolean
}
