package com.microsoft.device.dualscreen.twopanelayout.twopanelayoutnav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.microsoft.device.dualscreen.twopanelayout.Destination
import com.microsoft.device.dualscreen.twopanelayout.Screen
import com.microsoft.device.dualscreen.twopanelayout.common.calculatePaneSizes
import com.microsoft.device.dualscreen.twopanelayout.common.twoPaneMeasurePolicy
import com.microsoft.device.dualscreen.windowstate.WindowState

internal var isSinglePane = true
internal var navigatePane1To: NavHostController.(String) -> Unit = { _ -> }
internal var navigatePane2To: NavHostController.(String) -> Unit = { _ -> }
internal var navigateSinglePaneTo: NavHostController.(String, NavOptionsBuilder.() -> Unit) -> Unit = { _, _ -> }
internal var getPane1Destination: () -> String = { "" }
internal var getPane2Destination: () -> String = { "" }
internal var getSinglePaneDestination: () -> String = { "" }
internal val backStack = mutableListOf<TwoPaneBackStackEntry>()

internal fun isSinglePaneHandler(): Boolean {
    return isSinglePane
}

private fun findDestination(route: String, destinations: Array<Destination>): Destination {
    return destinations.find { pane -> pane.route == route }
        ?: throw IllegalArgumentException("Invalid route $route, not present in list of destinations $destinations")
}

@Composable
internal fun SinglePaneContainer(
    modifier: Modifier,
    destinations: Array<Destination>,
    startDestination: String,
    navController: NavHostController,
) {
    var currentSinglePane by remember { mutableStateOf(startDestination) }
    getSinglePaneDestination = { currentSinglePane }
    navigateSinglePaneTo = { route, navOptions ->
        val topDestination = backQueue.lastOrNull()?.destination?.route

        // Navigate only when necessary
        if (topDestination != route) {
            navigate(route, navOptions)
            currentSinglePane = route
        }
    }

    // Initialize backstack if empty
    if (backStack.isEmpty())
        backStack.initialize(startDestination)

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        destinations.forEach { pane ->
            composable(pane.route) {
                TwoPaneNavScopeInstance.(pane.content)()
            }
        }
    }
}

@Composable
internal fun TwoPaneContainer(
    windowState: WindowState,
    modifier: Modifier,
    destinations: Array<Destination>,
    pane1StartDestination: String,
    pane2StartDestination: String
) {
    // Initialize start destinations and handlers
    var currentPane1 by rememberSaveable { mutableStateOf(pane1StartDestination) }
    var currentPane2 by rememberSaveable { mutableStateOf(pane2StartDestination) }
    getPane1Destination = { currentPane1 }
    getPane2Destination = { currentPane2 }

    // Initialize navigation method handlers
    navigatePane1To = { route ->
        if (currentPane1 != route) {
            findDestination(route, destinations)
            currentPane1 = route
        }
    }
    navigatePane2To = { route ->
        if (currentPane2 != route) {
            findDestination(route, destinations)
            currentPane2 = route
        }
    }

    // Initialize backstack if empty
    if (backStack.isEmpty())
        backStack.initialize(pane1StartDestination, pane2StartDestination)

    // Find the destinations to display in each pane
    val pane1 = findDestination(currentPane1, destinations).content
    val pane2 = findDestination(currentPane2, destinations).content

    val measurePolicy = twoPaneMeasurePolicy(
        windowMode = windowState.windowMode,
        isSeparating = windowState.foldIsSeparating,
        paneSizes = calculatePaneSizes(windowState = windowState),
    )
    Layout(
        content = {
            TwoPaneNavScopeInstance.pane1()
            TwoPaneNavScopeInstance.pane2()
        },
        measurePolicy = measurePolicy,
        modifier = modifier
    )
}

/**
 * Helper method that executes after switching from two pane to single pane mode
 *
 * Copies manual backstack over to the navController backstack
 */
internal fun onSwitchToSinglePane(navController: NavHostController) {
    for (entry in backStack) {
        navController.navigateSinglePaneTo(entry.route) {}
    }
}

/**
 * Helper method that executes after switching from single pane to two pane mode
 *
 * Restores most recent pane 1 and 2 destinations, then resets manual backstack
 */
internal fun onSwitchToTwoPanes(
    navController: NavHostController,
    pane1StartDestination: String,
    pane2StartDestination: String
) {
    // Check if backstack has history to restore
    val pane1Route = backStack.findLast { it.launchScreen == Screen.Pane1 }?.route
    val pane2Route = backStack.findLast { it.launchScreen == Screen.Pane2 }?.route

    // Navigate to desired pane 1 and 2 destinations
    navController.navigatePane1To(pane1Route ?: pane1StartDestination)
    navController.navigatePane2To(pane2Route ?: pane2StartDestination)

    // Update backstack if history wasn't restored
    if (pane1Route == null)
        backStack.add(TwoPaneBackStackEntry(pane1StartDestination, Screen.Pane1))
    if (pane2Route == null)
        backStack.add(TwoPaneBackStackEntry(pane2StartDestination, Screen.Pane2))
}
