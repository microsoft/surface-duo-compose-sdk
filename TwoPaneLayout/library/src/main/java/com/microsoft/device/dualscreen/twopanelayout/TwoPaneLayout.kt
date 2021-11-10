/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.twopanelayout

import android.graphics.Rect
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.microsoft.device.dualscreen.twopanelayout.screenState.ConfigScreenState
import com.microsoft.device.dualscreen.twopanelayout.screenState.DeviceType
import com.microsoft.device.dualscreen.twopanelayout.screenState.LayoutOrientation
import com.microsoft.device.dualscreen.twopanelayout.screenState.LayoutState
import com.microsoft.device.dualscreen.twopanelayout.screenState.ScreenState

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
 * @param modifier The modifier to be applied to the TwoPane.
 */

@Composable
fun TwoPaneLayout(
    modifier: Modifier = Modifier,
    paneMode: TwoPaneMode = TwoPaneMode.TwoPane,
    pane1: @Composable TwoPaneScope.() -> Unit,
    pane2: @Composable TwoPaneScope.() -> Unit
) {
    var screenState by remember {
        mutableStateOf(
            ScreenState(
                deviceType = DeviceType.Single,
                screenSize = Size.Zero,
                hingeBounds = Rect(),
                orientation = LayoutOrientation.Horizontal,
                layoutState = LayoutState.Fold
            )
        )
    }
    ConfigScreenState(onStateChange = { screenState = it })

    val isSinglePane = isSinglePaneLayout(
        layoutState = screenState.layoutState,
        paneMode = paneMode,
        orientation = screenState.orientation
    )

    if (isSinglePane) {
        SinglePaneContainer(
            pane1 = pane1,
            pane2 = pane2
        )
    } else {
        TwoPaneContainer(
            screenState = screenState,
            modifier = modifier,
            pane1 = pane1,
            pane2 = pane2
        )
    }
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
private var currentSinglePane = Screen.Pane1.route

private sealed class Screen(val route: String) {
    object Pane1 : Screen("pane1")
    object Pane2 : Screen("pane2")
}

/*
 * The container to hold single pane for single-screen or single pane in dual-screen mode
 */
@Composable
internal fun SinglePaneContainer(
    pane1: @Composable TwoPaneScope.() -> Unit,
    pane2: @Composable TwoPaneScope.() -> Unit
) {
    val navController = rememberNavController()

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

/*
 * The container to hold the two panes for dual-screen/foldable/large-screen
 */
@Composable
private fun TwoPaneContainer(
    screenState: ScreenState,
    modifier: Modifier,
    pane1: @Composable TwoPaneScope.() -> Unit,
    pane2: @Composable TwoPaneScope.() -> Unit
) {
    val measurePolicy = twoPaneMeasurePolicy(
        orientation = screenState.orientation,
        paneSize = screenState.paneSize,
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
    layoutState: LayoutState,
    paneMode: TwoPaneMode,
    orientation: LayoutOrientation
): Boolean {
    return layoutState == LayoutState.Fold ||
        paneMode == TwoPaneMode.VerticalSingle && orientation == LayoutOrientation.Vertical ||
        paneMode == TwoPaneMode.HorizontalSingle && orientation == LayoutOrientation.Horizontal
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
