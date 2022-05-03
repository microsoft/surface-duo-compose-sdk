package com.microsoft.device.dualscreen.twopanelayout.twopanelayoutnav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.microsoft.device.dualscreen.twopanelayout.common.twoPaneMeasurePolicy
import com.microsoft.device.dualscreen.windowstate.WindowState


internal var isSinglePaneLayout = true // make a handler
private var currentSinglePane = mutableStateOf("")

internal var navigatePane1To: NavHostController.(String) -> Unit = { _: String -> }
internal var navigatePane2To: NavHostController.(String) -> Unit = { _: String -> }

internal fun NavHostController.navigateSinglePaneTo(route: String, navOptions: NavOptionsBuilder.() -> Unit) {
    navigate(route, navOptions)
    currentSinglePane.value = route
}

private fun findDestination(route: String, destinations: Array<Destination>): Destination {
    return destinations.find { pane -> pane.route == route }
        ?: throw IllegalArgumentException("Invalid route $route, not present in list of destinations $destinations")
}

@Composable
internal fun SinglePaneContainer(
    destinations: Array<Destination>,
    startDestination: String,
    navController: NavHostController,
) {
    currentSinglePane.value = startDestination

    NavHost(
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
    // Calculate pane sizes
    val pane1SizePx: Size
    val pane2SizePx: Size
    with(LocalDensity.current) {
        pane1SizePx = windowState.pane1SizeDp.toSize()
        pane2SizePx = windowState.pane2SizeDp.toSize()
    }

    // Initialize start destinations
    var currentPane1 by rememberSaveable { mutableStateOf(pane1StartDestination) }
    var currentPane2 by rememberSaveable { mutableStateOf(pane2StartDestination) }

    // Initialize navigation method handlers
    navigatePane1To = { route ->
        findDestination(route, destinations)
        currentPane1 = route
    }
    navigatePane2To = { route ->
        findDestination(route, destinations)
        currentPane2 = route
    }

    // Find the destinations to display in each pane
    val pane1 = findDestination(currentPane1, destinations).content
    val pane2 = findDestination(currentPane2, destinations).content

    val measurePolicy = twoPaneMeasurePolicy(
        windowMode = windowState.windowMode,
        isSeparating = windowState.foldIsSeparating,
        paneSizes = arrayOf(pane1SizePx, pane2SizePx),
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