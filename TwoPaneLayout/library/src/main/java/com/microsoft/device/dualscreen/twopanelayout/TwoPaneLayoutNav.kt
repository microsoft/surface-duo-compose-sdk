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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.microsoft.device.dualscreen.windowstate.WindowState
import com.microsoft.device.dualscreen.windowstate.rememberWindowState

data class Pane(val route: String, val content: @Composable TwoPaneScope.() -> Unit)
interface TwoPaneLayout {
    class PaneContainer private constructor(private val description: String) {

        override fun toString(): String {
            return description
        }

        companion object {
            val PANE1: PaneContainer = PaneContainer("Pane1")
            val PANE2: PaneContainer = PaneContainer("Pane2")
        }
    }
}

private var isSinglePaneLayout: Boolean = true
val isSinglePane: Boolean
    get() = isSinglePaneLayout

private var currentPane1 = mutableStateOf("")
val pane1Route: String
    get() = currentPane1.value

private var currentPane2 = mutableStateOf("")
val pane2Route: String
    get() = currentPane2.value

private var currentSinglePane = mutableStateOf("")
val singlePaneRoute: String
    get() = currentSinglePane.value

@Composable
fun TwoPaneLayout(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    paneMode: TwoPaneMode = TwoPaneMode.TwoPane,
    panes: Array<Pane>,
    singlePaneStartDestination: String,
    pane1StartDestination: String,
    pane2StartDestination: String
) {
    val activity = (LocalContext.current as? Activity)
        ?: throw ClassCastException("Local context could not be cast as an Activity")
    val windowState = activity.rememberWindowState()

    isSinglePaneLayout = isSinglePaneLayout(windowState.windowMode, paneMode)

    currentPane1.value = pane1StartDestination
    currentPane2.value = pane2StartDestination
    currentSinglePane.value = singlePaneStartDestination

    navigatePane1ToHandler = { route ->
        findPane(route, panes)
        currentPane1.value = route
    }
    navigatePane2ToHandler = { route ->
        findPane(route, panes)
        currentPane2.value = route
    }
    navigateToPaneHandler = { route, pane ->
        if (isSinglePane) {
            navigateSinglePaneTo(route)
        } else {
            pane ?: throw IllegalArgumentException("Origin pane cannot be null when in two pane mode")

            when (pane) {
                TwoPaneLayout.PaneContainer.PANE1 -> navigatePane1To(route)
                TwoPaneLayout.PaneContainer.PANE2 -> navigatePane2To(route)
            }
        }
    }

    if (isSinglePaneLayout) {
        SinglePaneContainer(
            navController = navController,
            panes = panes,
            startDestination = currentSinglePane.value
        )
    } else {
        TwoPaneContainer(
            windowState = windowState,
            modifier = modifier,
            pane1Route = currentPane1,
            pane2Route = currentPane2,
            panes = panes
        )
    }
}

@Composable
internal fun SinglePaneContainer(
    navController: NavHostController,
    panes: Array<Pane>,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        panes.forEach { pane ->
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
    pane1Route: MutableState<String>,
    pane2Route: MutableState<String>,
    panes: Array<Pane>
) {
    val pane1SizePx: Size
    val pane2SizePx: Size
    with(LocalDensity.current) {
        pane1SizePx = windowState.pane1SizeDp.toSize()
        pane2SizePx = windowState.pane2SizeDp.toSize()
    }

    val measurePolicy = twoPaneMeasurePolicy(
        windowMode = windowState.windowMode,
        isSeparating = windowState.foldIsSeparating,
        paneSizes = arrayOf(pane1SizePx, pane2SizePx),
    )

    val pane1 = findPane(pane1Route.value, panes)
    val pane2 = findPane(pane2Route.value, panes)

    Layout(
        content = {
            TwoPaneScopeInstance.(pane1.content)()
            TwoPaneScopeInstance.(pane2.content)()
        },
        measurePolicy = measurePolicy,
        modifier = modifier
    )
}

private var navigateToPaneHandler: NavHostController.(String, TwoPaneLayout.PaneContainer?) -> Unit =
    { _: String, _: TwoPaneLayout.PaneContainer? -> }
private var navigatePane1ToHandler: NavHostController.(String) -> Unit = { _: String -> }
private var navigatePane2ToHandler: NavHostController.(String) -> Unit = { _: String -> }

fun NavHostController.navigateToPane(route: String, pane: TwoPaneLayout.PaneContainer? = null) {
    navigateToPaneHandler(route, pane)
}

private fun NavHostController.navigateSinglePaneTo(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

    currentSinglePane.value = route
}

private fun NavHostController.navigatePane1To(route: String) {
    navigatePane1ToHandler(route)
    // TODO: save stack somehow?
}

private fun NavHostController.navigatePane2To(route: String) {
    navigatePane2ToHandler(route)
}

private fun findPane(route: String, panes: Array<Pane>): Pane {
    return panes.find { pane -> pane.route == route }
        ?: throw IllegalArgumentException("Invalid route $route, not present in list of panes $panes")
}
