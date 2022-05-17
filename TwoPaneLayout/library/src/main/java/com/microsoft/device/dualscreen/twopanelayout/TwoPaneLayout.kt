/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.twopanelayout

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.microsoft.device.dualscreen.twopanelayout.twopanelayout.SinglePaneContainer
import com.microsoft.device.dualscreen.twopanelayout.twopanelayout.TwoPaneContainer
import com.microsoft.device.dualscreen.twopanelayout.twopanelayout.isSinglePane
import com.microsoft.device.dualscreen.twopanelayout.twopanelayout.isSinglePaneLayout
import com.microsoft.device.dualscreen.twopanelayout.twopanelayoutnav.SinglePaneContainer
import com.microsoft.device.dualscreen.twopanelayout.twopanelayoutnav.TwoPaneContainer
import com.microsoft.device.dualscreen.windowstate.rememberWindowState
import com.microsoft.device.dualscreen.twopanelayout.twopanelayoutnav.isSinglePane as isSinglePaneNav

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
    pane2: @Composable TwoPaneScope.() -> Unit,
    onPaneIncrease: TwoPaneScope.() -> Unit = {},
    onPaneDecrease: TwoPaneScope.() -> Unit = {}
) {
    val navController = rememberNavController()

    TwoPaneLayout(modifier, paneMode, navController, pane1, pane2, onPaneIncrease, onPaneDecrease)
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
 * @param onPaneIncrease: method to execute when switching from single pane to two panes
 * @param onPaneDecrease: method to execute when switching from two panes to single pane
 */
@Composable
fun TwoPaneLayout(
    modifier: Modifier = Modifier,
    paneMode: TwoPaneMode = TwoPaneMode.TwoPane,
    navController: NavHostController,
    pane1: @Composable TwoPaneScope.() -> Unit,
    pane2: @Composable TwoPaneScope.() -> Unit,
    onPaneIncrease: TwoPaneScope.() -> Unit = {},
    onPaneDecrease: TwoPaneScope.() -> Unit = {}
) {
    val activity = (LocalContext.current as? Activity)
        ?: throw ClassCastException("Local context could not be cast as an Activity")
    val windowState = activity.rememberWindowState()

    val prevIsSinglePane = isSinglePane
    isSinglePane = isSinglePaneLayout(windowState.windowMode, paneMode)

    // If switching from single pane to two panes or vice versa, set up pane change methods
    val isPaneDecrease = isSinglePane && !prevIsSinglePane
    val isPaneIncrease = !isSinglePane && prevIsSinglePane

    if (isSinglePane) {
        SinglePaneContainer(
            navController = navController,
            pane1 = pane1,
            pane2 = pane2,
            isPaneDecrease = isPaneDecrease,
            onPaneDecrease = onPaneDecrease
        )
    } else {
        TwoPaneContainer(
            windowState = windowState,
            modifier = modifier,
            pane1 = pane1,
            pane2 = pane2,
            isPaneIncrease = isPaneIncrease,
            onPaneIncrease = onPaneIncrease
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
 * Like [TwoPaneLayout], the [TwoPaneLayoutNav] layout is able to assign children widths or heights according to their weights
 * provided using the [TwoPaneNavScope.weight] modifier. If all the children have not provided a weight,
 * they will be layout equally, with the potential padding in-between based on the
 * physical hinge between two screens.
 *
 * Instead of the fixed pane 1 and pane 2 parameters in the original [TwoPaneLayout], [TwoPaneLayoutNav] was made
 * to be more flexible and customizable by accepting NavHostController parameters and an array of multiple
 * destinations. In single pane mode, navigation is done with a NavHost, but in two pane mode, the content in
 * each screen/pane is updated manually by changing the content shown in the layout.
 *
 * @param modifier: The modifier to be applied to the TwoPaneLayout
 * @param navController: The navController to use when navigating within the single pane container
 * @param paneMode: The [TwoPaneMode] that determines when one or two panes are shown
 * @param destinations: The destinations that will be displayed in the layout
 * @param singlePaneStartDestination: The start destination for single pane mode
 * @param pane1StartDestination: The start destination for pane 1 in two pane mode
 * @param pane2StartDestination: The start destination for pane 2 in two pane mode
 * @param onPaneIncrease: method to execute when switching from single pane to two panes
 * @param onPaneDecrease: method to execute when switching from two panes to single pane
 */
@Composable
fun TwoPaneLayoutNav(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    paneMode: TwoPaneMode = TwoPaneMode.TwoPane,
    destinations: Array<Destination>,
    singlePaneStartDestination: String,
    pane1StartDestination: String,
    pane2StartDestination: String,
    onPaneIncrease: TwoPaneNavScope.() -> Unit = {},
    onPaneDecrease: TwoPaneNavScope.() -> Unit = {}
) {
    val activity = (LocalContext.current as? Activity)
        ?: throw ClassCastException("Local context could not be cast as an Activity")
    val windowState = activity.rememberWindowState()

    val prevIsSinglePane = isSinglePaneNav
    isSinglePaneNav = isSinglePaneLayout(windowState.windowMode, paneMode)

    // If switching from single pane to two panes or vice versa, set up pane change methods
    val isPaneDecrease = isSinglePaneNav && !prevIsSinglePane
    val isPaneIncrease = !isSinglePaneNav && prevIsSinglePane

    if (isSinglePaneNav) {
        SinglePaneContainer(
            destinations = destinations,
            startDestination = singlePaneStartDestination,
            navController = navController,
            isPaneDecrease = isPaneDecrease,
            onPaneDecrease = onPaneDecrease
        )
    } else {
        TwoPaneContainer(
            windowState = windowState,
            modifier = modifier,
            destinations = destinations,
            pane1StartDestination = pane1StartDestination,
            pane2StartDestination = pane2StartDestination,
            isPaneIncrease = isPaneIncrease,
            onPaneIncrease = onPaneIncrease
        )
    }
}
