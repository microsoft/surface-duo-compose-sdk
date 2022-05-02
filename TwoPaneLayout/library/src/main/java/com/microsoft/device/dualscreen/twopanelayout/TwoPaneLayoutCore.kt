package com.microsoft.device.dualscreen.twopanelayout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.microsoft.device.dualscreen.windowstate.WindowMode
import com.microsoft.device.dualscreen.windowstate.WindowState

/**
 * The route of the pane shown in the SinglePaneContainer
 */
private var currentSinglePane = Screen.Pane1.route

internal var navigateToPane1Handler: () -> Unit = {}
internal var navigateToPane2Handler: () -> Unit = {}
internal var isPane1ShownHandler: () -> Boolean = { currentSinglePane == Screen.Pane1.route }

/**
 * The container to hold single pane for single-screen or single pane in dual-screen mode
 */
@Composable
internal fun SinglePaneContainer(
    navController: NavHostController,
    pane1: @Composable TwoPaneScope.() -> Unit,
    pane2: @Composable TwoPaneScope.() -> Unit
) {
    currentSinglePane = Screen.Pane1.route // always start from Pane1 to maintain the expected backstack

    NavHost(
        navController = navController,
        startDestination = currentSinglePane
    ) {
        composable(Screen.Pane1.route) {
            TwoPaneScopeInstance.pane1()
        }
        composable(Screen.Pane2.route) {
            TwoPaneScopeInstance.pane2()
        }
    }

    navigateToPane1Handler = {
        if (!navController.backQueue.isEmpty()) {
            val topPane = navController.backQueue.last().destination.route

            // Navigate only when pane1 is not shown(not at the top of the backstack)
            if (topPane != Screen.Pane1.route) {
                navController.popBackStack()
            }
        }

        currentSinglePane = Screen.Pane1.route
    }

    navigateToPane2Handler = {
        if (!navController.backQueue.isEmpty()) {
            val topPane = navController.backQueue.last().destination.route

            // Navigate only when pane2 is not shown(not at the top of the backstack)
            if (topPane != Screen.Pane2.route) {
                navController.navigate(Screen.Pane2.route)
            }
        }
        currentSinglePane = Screen.Pane2.route
    }
}

/**
 * The container to hold the two panes for dual-screen/foldable/large-screen
 */
@Composable
internal fun TwoPaneContainer(
    windowState: WindowState,
    modifier: Modifier,
    pane1: @Composable TwoPaneScope.() -> Unit,
    pane2: @Composable TwoPaneScope.() -> Unit,
) {
    val pane1SizePx: Size
    val pane2SizePx: Size
    with(LocalDensity.current) {
        pane1SizePx = windowState.pane1SizeDp.toSize()
        pane2SizePx = windowState.pane2SizeDp.toSize()
    }

    navigateToPane1Handler = { }
    navigateToPane2Handler = { }

    val measurePolicy = twoPaneMeasurePolicy(
        windowMode = windowState.windowMode,
        isSeparating = windowState.foldIsSeparating,
        paneSizes = arrayOf(pane1SizePx, pane2SizePx),
    )
    Layout(
        content = {
            TwoPaneScopeInstance.pane1()
            TwoPaneScopeInstance.pane2()
        },
        measurePolicy = measurePolicy,
        modifier = modifier
    )
}

internal fun isSinglePaneLayout(
    windowMode: WindowMode,
    paneMode: TwoPaneMode,
): Boolean {
    return !windowMode.isDualScreen ||
        paneMode == TwoPaneMode.VerticalSingle && windowMode == WindowMode.DUAL_PORTRAIT ||
        paneMode == TwoPaneMode.HorizontalSingle && windowMode == WindowMode.DUAL_LANDSCAPE
}
