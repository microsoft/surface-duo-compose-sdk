package com.microsoft.device.dualscreen.twopanelayout

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.microsoft.device.dualscreen.windowstate.rememberWindowState

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
 */
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
            pane1StartDestination = pane1StartDestination,
            pane2StartDestination = pane2StartDestination
        )
    }
}
