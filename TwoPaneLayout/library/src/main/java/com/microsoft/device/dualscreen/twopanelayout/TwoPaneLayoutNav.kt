package com.microsoft.device.dualscreen.twopanelayout

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.microsoft.device.dualscreen.windowstate.WindowState
import com.microsoft.device.dualscreen.windowstate.rememberWindowState

private var isSinglePaneLayout: Boolean = true
val isSinglePane: Boolean
    get() = isSinglePaneLayout

private var currentPane1 = mutableStateOf("")
private var currentPane2 = mutableStateOf("")
private var currentSinglePane = mutableStateOf("")

@Composable
fun TwoPaneLayout(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    paneMode: TwoPaneMode = TwoPaneMode.TwoPane,
    destinations: Array<Destination>,
    singlePaneStartDestination: String,
    pane1StartDestination: String,
    pane2StartDestination: String
) {
    val activity = (LocalContext.current as? Activity)
        ?: throw ClassCastException("Local context could not be cast as an Activity")
    val windowState = activity.rememberWindowState()

    isSinglePaneLayout = isSinglePaneLayout(windowState.windowMode, paneMode)

    // Initialize start destinations for all panes
    currentPane1.value = pane1StartDestination
    currentPane2.value = pane2StartDestination
    currentSinglePane.value = singlePaneStartDestination

    navigatePane1ToHandler = { route ->
        findPane(route, destinations)
        currentPane1.value = route
    }
    navigatePane2ToHandler = { route ->
        findPane(route, destinations)
        currentPane2.value = route
    }
    navigateToPaneHandler = { route, navOptions, pane ->
        if (isSinglePane) {
            navigateSinglePaneTo(route, navOptions)
        } else {
            pane ?: throw IllegalArgumentException("Origin pane cannot be null when in two pane mode")

            when (pane) {
                PaneContainer.Pane1 -> navigatePane1To(route)
                PaneContainer.Pane2 -> navigatePane2To(route)
            }
        }
    }

    if (isSinglePaneLayout) {
        SinglePaneContainer(
            destinations = destinations,
            startDestination = singlePaneStartDestination,
            navController = navController,
        )
    } else {
        TwoPaneContainer(
            windowState = windowState,
            modifier = modifier,
            destinations = destinations,
            pane1Route = currentPane1,
            pane2Route = currentPane2
        )
    }
}

@Composable
private fun SinglePaneContainer(
    destinations: Array<Destination>,
    startDestination: String,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        destinations.forEach { pane ->
            composable(pane.route) {
                TwoPaneScopeInstance.(pane.content)()
            }
        }
    }
}

@Composable
private fun TwoPaneContainer(
    windowState: WindowState,
    modifier: Modifier,
    destinations: Array<Destination>,
    pane1Route: MutableState<String>,
    pane2Route: MutableState<String>
) {
    TwoPaneContainer(
        windowState = windowState,
        modifier = modifier,
        pane1 = findPane(pane1Route.value, destinations).content,
        pane2 = findPane(pane2Route.value, destinations).content,
    )
}

private var navigateToPaneHandler: NavHostController.(String, NavOptionsBuilder.() -> Unit, PaneContainer?) -> Unit =
    { _: String, _: NavOptionsBuilder.() -> Unit, _: PaneContainer? -> }
private var navigatePane1ToHandler: NavHostController.(String) -> Unit = { _: String -> }
private var navigatePane2ToHandler: NavHostController.(String) -> Unit = { _: String -> }

fun NavHostController.navigateToPane(
    route: String,
    navOptions: NavOptionsBuilder.() -> Unit = { },
    pane: PaneContainer? = null
) {
    navigateToPaneHandler(route, navOptions, pane)
}

private fun NavHostController.navigateSinglePaneTo(route: String, navOptions: NavOptionsBuilder.() -> Unit) {
    navigate(route, navOptions)
    currentSinglePane.value = route
}

private fun NavHostController.navigatePane1To(route: String) {
    navigatePane1ToHandler(route)
    // TODO: save stack somehow?
}

private fun NavHostController.navigatePane2To(route: String) {
    navigatePane2ToHandler(route)
}

private fun findPane(route: String, destinations: Array<Destination>): Destination {
    return destinations.find { pane -> pane.route == route }
        ?: throw IllegalArgumentException("Invalid route $route, not present in list of panes $destinations")
}
