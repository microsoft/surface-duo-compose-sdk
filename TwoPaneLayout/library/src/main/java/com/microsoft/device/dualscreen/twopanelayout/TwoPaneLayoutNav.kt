package com.microsoft.device.dualscreen.twopanelayout

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.microsoft.device.dualscreen.windowstate.rememberWindowState

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
