/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.navigationrail.ui.components

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.NavigationRail
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.microsoft.device.display.samples.navigationrail.R
import com.microsoft.device.display.samples.navigationrail.ui.view.GallerySections
import com.microsoft.device.display.samples.navigationrail.ui.view.NAV_TAG
import com.microsoft.device.display.samples.navigationrail.ui.view.defaultNavOptions
import com.microsoft.device.dualscreen.twopanelayout.Screen
import com.microsoft.device.dualscreen.twopanelayout.TwoPaneNavScope

private val NAV_RAIL_TOP_SPACING = 32.dp

@ExperimentalAnimationApi
@Composable
fun TwoPaneNavScope.GalleryNavRail(
    navController: NavHostController,
    galleries: Array<GallerySections>,
    updateImageId: (Int?) -> Unit,
    updateRoute: (String) -> Unit,
) {
    val twoPaneNavScope = this

    NavigationRail(
        modifier = Modifier.testTag(stringResource(id = R.string.nav_rail)),
        backgroundColor = MaterialTheme.colors.primary,
    ) {
        Spacer(Modifier.height(NAV_RAIL_TOP_SPACING))
        val currentDestination =
            if (twoPaneNavScope.isSinglePane)
                navController.currentBackStackEntryAsState().value?.destination?.route
            else
                twoPaneNavScope.currentPane1Destination
        galleries.forEach { gallery ->
            NavRailItemWithSelector(
                icon = {
                    NavItemIcon(icon = gallery.icon, description = stringResource(gallery.title))
                },
                label = { NavItemLabel(stringResource(gallery.title)) },
                selected = isNavItemSelected(currentDestination, gallery.route),
                onClick = {
                    twoPaneNavScope.navItemOnClick(navController, gallery.route, updateImageId, updateRoute)
                },
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = MaterialTheme.colors.onPrimary
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun TwoPaneNavScope.GalleryBottomNav(
    navController: NavHostController,
    galleries: Array<GallerySections>,
    updateImageId: (Int?) -> Unit,
    updateRoute: (String) -> Unit,
) {
    val twoPaneNavScope = this

    BottomNavigation(
        modifier = Modifier.testTag(stringResource(id = R.string.bottom_nav)),
        backgroundColor = MaterialTheme.colors.primary,
    ) {
        val currentDestination =
            if (twoPaneNavScope.isSinglePane)
                navController.currentBackStackEntryAsState().value?.destination?.route
            else
                twoPaneNavScope.currentPane1Destination
        galleries.forEach { gallery ->
            BottomNavItemWithSelector(
                icon = {
                    NavItemIcon(icon = gallery.icon, description = stringResource(gallery.title))
                },
                label = { NavItemLabel(stringResource(gallery.title)) },
                selected = isNavItemSelected(currentDestination, gallery.route),
                onClick = {
                    twoPaneNavScope.navItemOnClick(navController, gallery.route, updateImageId, updateRoute)
                },
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = MaterialTheme.colors.onPrimary
            )
        }
    }
}

@Composable
private fun NavItemIcon(icon: Int, description: String) {
    Icon(painterResource(icon), description)
}

@Composable
private fun NavItemLabel(navItem: String) {
    Text(navItem)
}

/**
 * Reference: https://developer.android.com/jetpack/compose/navigation#bottom-nav
 */
@Composable
private fun isNavItemSelected(
    currentDestination: String?,
    navItemRoute: String,
): Boolean {
    return currentDestination == navItemRoute // currentDestination?.hierarchy?.any { it.route == navItemRoute } == true
}

/**
 * Reference: https://developer.android.com/jetpack/compose/navigation#bottom-nav
 */
private fun TwoPaneNavScope.navItemOnClick(
    navController: NavHostController,
    navItem: String,
    updateImageId: (Int?) -> Unit,
    updateRoute: (String) -> Unit,
) {
    // Navigate to new destination
    navController.navigateTo(navItem, Screen.Pane1, defaultNavOptions)
    Log.d(NAV_TAG, "navItemOnClick -> navigating to $navItem")

    // Update current route to new destination
    updateRoute(navItem)

    // Reset selected image when switching gallery
    updateImageId(null)
}
