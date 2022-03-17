/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.twopanelayout

import android.app.Activity
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.microsoft.device.dualscreen.windowstate.WindowMode
import com.microsoft.device.dualscreen.windowstate.WindowState
import com.microsoft.device.dualscreen.windowstate.rememberWindowState

/**
 * TwoPaneMode
 * TwoPane,          always shows two panes, regardless the orientation, by default
 * HorizontalSingle  shows big single pane in horizontal orientation layout(top/bottom)
 * VerticalSingle    shows big single pane in vertical orientation layout(left/right)
 */
enum class TwoPaneMode {
    TwoPane,
    HorizontalSingle,
    VerticalSingle
}

/**
 * A layout component that places its children in one or two panes vertically or horizontally to
 * support the layout on foldable or dual-screen form factors. One-pane can be used to layout on
 * the single-screen device, or single-screen mode on the foldable or dual-screen devices. Two-pane
 * can be used to layout left/right or top/bottom screens on the foldable or dual-screen devices.
 * The tablet or wide screen devices will display two-pane layout by default.
 *
 * The [TwoPaneLayout] layout is able to assign children widths or heights according to their weights
 * provided using the [TwoPaneScope.weight] modifier. If all the children have not provided a weight,
 * they will be layout equally, with the potential padding in-between based on the
 * physical hinge between two screens.
 *
 * @param modifier: The modifier to be applied to the TwoPaneLayout
 * @param paneMode: The [TwoPaneMode] that determines when one or two panes are shown
 * @param navController: The navController to use when navigating within the single pane container
 * @param pane1: The content to show in pane 1
 * @param pane2: The content to show in pane 2
 */
@Composable
fun TwoPaneLayout(
    modifier: Modifier = Modifier,
    paneMode: TwoPaneMode = TwoPaneMode.TwoPane,
    navController: NavHostController,
    pane1: @Composable TwoPaneScope.() -> Unit,
    pane2: @Composable TwoPaneScope.() -> Unit
) {
    val activity = (LocalContext.current as? Activity)
        ?: throw ClassCastException("Local context could not be cast as an Activity")
    val windowState = activity.rememberWindowState()

    val isSinglePane = isSinglePaneLayout(windowState.windowMode, paneMode)

    if (isSinglePane) {
        SinglePaneContainer(
            navController = navController,
            pane1 = pane1,
            pane2 = pane2
        )
    } else {
        TwoPaneContainer(
            windowState = windowState,
            modifier = modifier,
            pane1 = pane1,
            pane2 = pane2
        )
    }
}

/**
 * A layout component that places its children in one or two panes vertically or horizontally to
 * support the layout on foldable or dual-screen form factors. One-pane can be used to layout on
 * the single-screen device, or single-screen mode on the foldable or dual-screen devices. Two-pane
 * can be used to layout left/right or top/bottom screens on the foldable or dual-screen devices.
 * The tablet or wide screen devices will display two-pane layout by default.
 *
 * The [TwoPaneLayout] layout is able to assign children widths or heights according to their weights
 * provided using the [TwoPaneScope.weight] modifier. If all the children have not provided a weight,
 * they will be layout equally, with the potential padding in-between based on the
 * physical hinge between two screens.
 *
 * @param modifier: The modifier to be applied to the TwoPaneLayout
 * @param paneMode: The [TwoPaneMode] that determines when one or two panes are shown
 * @param pane1: The content to show in pane 1
 * @param pane2: The content to show in pane 2
 */
@Composable
fun TwoPaneLayout(
    modifier: Modifier = Modifier,
    paneMode: TwoPaneMode = TwoPaneMode.TwoPane,
    pane1: @Composable TwoPaneScope.() -> Unit,
    pane2: @Composable TwoPaneScope.() -> Unit
) {
    val navController = rememberNavController()

    TwoPaneLayout(modifier, paneMode, navController, pane1, pane2)
}

/**
 * Navigation to the first pane in the single-pane mode
 */
fun navigateToPane1() {
    navigateToPane1Handler()
}

/**
 * Navigation to the second pane in the single-pane mode
 */
fun navigateToPane2() {
    navigateToPane2Handler()
}

private lateinit var navigateToPane1Handler: () -> Unit
private lateinit var navigateToPane2Handler: () -> Unit

/**
 * The route of the pane shown in the SinglePaneContainer
 */
var currentSinglePane = Screen.Pane1.route

/**
 * Class that represents a screen in a NavHost
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

/**
 * The container to hold single pane for single-screen or single pane in dual-screen mode
 */
@Composable
internal fun SinglePaneContainer(
    navController: NavHostController,
    pane1: @Composable TwoPaneScope.() -> Unit,
    pane2: @Composable TwoPaneScope.() -> Unit
) {
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
        navController.navigate(Screen.Pane1.route)
        currentSinglePane = Screen.Pane1.route
    }

    navigateToPane2Handler = {
        navController.navigate(Screen.Pane2.route)
        currentSinglePane = Screen.Pane2.route
    }
}

/**
 * The container to hold the two panes for dual-screen/foldable/large-screen
 */
@Composable
private fun TwoPaneContainer(
    windowState: WindowState,
    modifier: Modifier,
    pane1: @Composable TwoPaneScope.() -> Unit,
    pane2: @Composable TwoPaneScope.() -> Unit
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

@LayoutScopeMarker
@Immutable
interface TwoPaneScope {
    @Stable
    fun Modifier.weight(
        weight: Float,
    ): Modifier
}

internal object TwoPaneScopeInstance : TwoPaneScope {
    @Stable
    override fun Modifier.weight(weight: Float): Modifier {
        require(weight > 0.0) { "invalid weight $weight; must be greater than zero" }
        return this.then(
            LayoutWeightImpl(
                weight = weight,
                inspectorInfo = debugInspectorInfo {
                    name = "weight"
                    value = weight
                    properties["weight"] = weight
                }
            )
        )
    }
}
