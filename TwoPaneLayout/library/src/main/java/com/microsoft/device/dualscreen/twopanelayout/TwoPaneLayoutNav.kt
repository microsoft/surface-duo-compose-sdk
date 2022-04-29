package com.microsoft.device.dualscreen.twopanelayout

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.microsoft.device.dualscreen.windowstate.WindowState
import com.microsoft.device.dualscreen.windowstate.rememberWindowState

private var isSinglePaneLayout = true
private var currentPane1 = mutableStateOf("")
private var currentPane2 = mutableStateOf("")
private var currentSinglePane = mutableStateOf("")

internal var navigatePane1To: NavHostController.(String) -> Unit = { _: String -> }
internal var navigatePane2To: NavHostController.(String) -> Unit = { _: String -> }

internal fun isSinglePaneLayout(): Boolean {
    return isSinglePaneLayout
}

internal fun NavHostController.navigateSinglePaneTo(route: String, navOptions: NavOptionsBuilder.() -> Unit) {
    navigate(route, navOptions)
    currentSinglePane.value = route
}

private fun findPane(route: String, destinations: Array<Destination>): Destination {
    return destinations.find { pane -> pane.route == route }
        ?: throw IllegalArgumentException("Invalid route $route, not present in list of panes $destinations")
}

@Composable
fun TwoPaneLayoutNav(
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

    // Initialize navigation method handlers
    navigatePane1To = { route ->
        findPane(route, destinations)
        currentPane1.value = route
    }
    navigatePane2To = { route ->
        findPane(route, destinations)
        currentPane2.value = route
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
                TwoPaneNavScopeInstance.(pane.content)()
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
    val pane1SizePx: Size
    val pane2SizePx: Size
    with(LocalDensity.current) {
        pane1SizePx = windowState.pane1SizeDp.toSize()
        pane2SizePx = windowState.pane2SizeDp.toSize()
    }

    val pane1 = findPane(pane1Route.value, destinations).content
    val pane2 = findPane(pane2Route.value, destinations).content

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