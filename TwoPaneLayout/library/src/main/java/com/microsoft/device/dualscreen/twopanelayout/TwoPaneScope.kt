package com.microsoft.device.dualscreen.twopanelayout

import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
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
     * @param launchScreen: the screen (pane 1 or pane 2) in which to launch content in two pane mode
     * @param builder: builder to create a new [NavOptions]
     */
    @Stable
    fun NavHostController.navigateTo(
        route: String,
        launchScreen: Screen,
        builder: NavOptionsBuilder.() -> Unit = { }
    )

    /**
     * Navigates up to the previous destination on the backstack. In single pane mode, this calls
     * [NavHostController.navigateUp]. In two pane mode, this pops the most recent back stack entry off the stack.
     *
     * If called on the last entry in the back stack (last two entries in dual screen mode), the activity will
     * be finished.
     *
     * @return true if navigation was successful, false otherwise
     */
    @Stable
    fun NavHostController.navigateBack(): Boolean

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
