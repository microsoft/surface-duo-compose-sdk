/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.navigationrail.ui.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.navigation.compose.rememberNavController
import com.microsoft.device.dualscreen.twopanelayout.TwoPaneLayoutNav
import com.microsoft.device.dualscreen.twopanelayout.TwoPaneMode
import com.microsoft.device.dualscreen.windowstate.WindowState

@ExperimentalAnimationApi
@ExperimentalUnitApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun NavigationRailApp(windowState: WindowState) {
    // Set up starting route for navigation in pane 1
    var currentRoute by rememberSaveable { mutableStateOf(navDestinations.first().route) }
    val updateRoute: (String) -> Unit = { newRoute -> currentRoute = newRoute }

    // Set up variable to store selected image id
    var imageId: Int? by rememberSaveable { mutableStateOf(null) }
    val updateImageId: (Int?) -> Unit = { newId -> imageId = newId }

    NavigationRailAppContent(
        isDualScreen = windowState.isDualScreen(),
        isDualPortrait = windowState.isDualPortrait(),
        isDualLandscape = windowState.isDualLandscape(),
        foldIsOccluding = windowState.foldIsOccluding,
        foldBoundsDp = windowState.foldBoundsDp,
        windowHeight = windowState.windowHeightDp,
        imageId = imageId,
        updateImageId = updateImageId,
        currentRoute = currentRoute,
        updateRoute = updateRoute
    )
}

@ExperimentalUnitApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun NavigationRailAppContent(
    isDualScreen: Boolean,
    isDualPortrait: Boolean,
    isDualLandscape: Boolean,
    foldIsOccluding: Boolean,
    foldBoundsDp: DpRect,
    windowHeight: Dp,
    imageId: Int?,
    updateImageId: (Int?) -> Unit,
    currentRoute: String,
    updateRoute: (String) -> Unit
) {
    // Create nav controller
    val navController = rememberNavController()

    // Create app destinations for TwoPaneLayoutNav
    val appDestinations = appDestinations(
        isDualScreen = isDualScreen,
        navController = navController,
        imageId = imageId,
        updateImageId = updateImageId,
        currentRoute = currentRoute,
        updateRoute = updateRoute,
        isDualPortrait = isDualPortrait,
        isDualLandscape = isDualLandscape,
        foldIsOccluding = foldIsOccluding,
        foldBoundsDp = foldBoundsDp,
        windowHeight = windowHeight
    )
    TwoPaneLayoutNav(
        paneMode = TwoPaneMode.HorizontalSingle,
        navController = navController,
        destinations = appDestinations,
        singlePaneStartDestination = navDestinations.first().route,
        pane1StartDestination = navDestinations.first().route,
        pane2StartDestination = ITEM_DETAIL_ROUTE
    )
}
