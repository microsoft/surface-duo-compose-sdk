/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.navigationrail.ui.view

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import com.microsoft.device.display.samples.navigationrail.R
import com.microsoft.device.display.samples.navigationrail.models.DataProvider
import com.microsoft.device.display.samples.navigationrail.models.Image
import com.microsoft.device.dualscreen.twopanelayout.Destination

// Nav destinations for app
val navDestinations = GallerySections.values()
const val ITEM_DETAIL_ROUTE = "item detail"

val defaultNavOptions: NavOptionsBuilder.() -> Unit = {
// REVISIT: we can pop destinations off the stack if we want
//    popUpTo(navDestinations.first().route) {
//        saveState = true
//    }
    launchSingleTop = true
    restoreState = true
}
val itemDetailNavOptions: NavOptionsBuilder.() -> Unit = {
    popUpTo(navDestinations.first().route) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = true
}

const val NAV_TAG = "Navigation debugging"
internal fun logBackQueue(navController: NavHostController, tag: String = NAV_TAG) {
    Log.d(
        tag,
        "back queue (${navController.backQueue.size}) " +
            navController.backQueue.map { it.destination.route }.joinToString(", ")
    )
}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
@ExperimentalMaterialApi
@ExperimentalUnitApi
fun appDestinations(
    isDualScreen: Boolean,
    navController: NavHostController,
    imageId: Int?,
    updateImageId: (Int?) -> Unit,
    currentRoute: String,
    updateRoute: (String) -> Unit,
    isDualPortrait: Boolean,
    isDualLandscape: Boolean,
    foldIsOccluding: Boolean,
    foldBoundsDp: DpRect,
    windowHeight: Dp,
): Array<Destination> {
    return galleryDestinations(
        isDualScreen = isDualScreen,
        navController = navController,
        imageId = imageId,
        updateImageId = updateImageId,
        currentRoute = currentRoute,
        updateRoute = updateRoute
    ).plus(
        itemDetailDestination(
            isDualPortrait = isDualPortrait,
            isDualLandscape = isDualLandscape,
            foldIsOccluding = foldIsOccluding,
            foldBoundsDp = foldBoundsDp,
            windowHeight = windowHeight,
            imageId = imageId,
            updateImageId = updateImageId,
            currentRoute = currentRoute,
            navController = navController
        )
    )
}

enum class GallerySections(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val route: String,
    val list: List<Image>,
    @DrawableRes val placeholderImage: Int,
    @DrawableRes val fact1Icon: Int,
    @StringRes val fact1Description: Int,
    @DrawableRes val fact2Icon: Int? = null,
    @StringRes val fact2Description: Int? = null,
) {
    PLANTS(
        R.string.plants,
        R.drawable.plant_icon,
        "plants",
        DataProvider.plantList,
        R.drawable.plants_placeholder,
        R.drawable.sun_icon,
        R.string.sun,
        R.drawable.plant_height_icon,
        R.string.height
    ),
    BIRDS(
        R.string.birds,
        R.drawable.bird_icon,
        "birds",
        DataProvider.birdList,
        R.drawable.birds_placeholder,
        R.drawable.wingspan_icon,
        R.string.bird_size,
    ),
    ANIMALS(
        R.string.animals,
        R.drawable.animal_icon,
        "animals",
        DataProvider.animalList,
        R.drawable.animals_placeholder,
        R.drawable.animal_size_icon,
        R.string.animal_size,
    ),
    LAKES(
        R.string.lakes,
        R.drawable.lake_icon,
        "lakes",
        DataProvider.lakeList,
        R.drawable.lakes_placeholder,
        R.drawable.sea_level_icon,
        R.string.sea_level,
    ),
    ROCKS(
        R.string.rocks,
        R.drawable.rock_icon,
        "rocks",
        DataProvider.rockList,
        R.drawable.rocks_placeholder,
        R.drawable.chemical_constituents_icon,
        R.string.composition
    )
}
