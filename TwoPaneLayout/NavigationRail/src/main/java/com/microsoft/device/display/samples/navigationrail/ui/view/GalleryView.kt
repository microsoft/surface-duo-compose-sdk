/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.navigationrail.ui.view

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.microsoft.device.display.samples.navigationrail.R
import com.microsoft.device.display.samples.navigationrail.models.Image
import com.microsoft.device.display.samples.navigationrail.ui.components.GalleryBottomNav
import com.microsoft.device.display.samples.navigationrail.ui.components.GalleryNavRail
import com.microsoft.device.display.samples.navigationrail.ui.components.GalleryTopBar
import com.microsoft.device.dualscreen.twopanelayout.Destination
import com.microsoft.device.dualscreen.twopanelayout.Screen
import com.microsoft.device.dualscreen.twopanelayout.TwoPaneNavScope

private val BORDER_SIZE = 7.dp
private val GALLERY_SPACING = 2.dp
private const val NUM_COLUMNS = 2
private val GALLERY_HORIZ_PADDING = 16.dp

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun galleryDestinations(
    isDualScreen: Boolean,
    navController: NavHostController,
    imageId: Int?,
    updateImageId: (Int?) -> Unit,
    currentRoute: String,
    updateRoute: (String) -> Unit
): Array<Destination> {
    val horizontalPadding = GALLERY_HORIZ_PADDING

    return navDestinations.map { section ->
        Destination(route = section.route) {
            GalleryViewWithTopBar(
                section = section,
                horizontalPadding = horizontalPadding,
                navController = navController,
                isDualScreen = isDualScreen,
                imageId = imageId,
                updateImageId = updateImageId,
                currentRoute = currentRoute,
                updateRoute = updateRoute
            )
        }
    }.toTypedArray()
}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun TwoPaneNavScope.GalleryViewWithTopBar(
    section: GallerySections,
    horizontalPadding: Dp,
    navController: NavHostController,
    isDualScreen: Boolean,
    imageId: Int?,
    updateImageId: (Int?) -> Unit,
    currentRoute: String,
    updateRoute: (String) -> Unit
) {
    BackHandler(enabled = isSinglePane && navController.backQueue.size > 2) {
        navController.previousBackStackEntry?.destination?.route?.let { updateRoute(it) }
        navController.navigateUp()
        Log.d(NAV_TAG, "onBackPressed in GalleryView")
        logBackQueue(navController)
    }

    // Make sure the current gallery section should be displayed
    if (isSinglePane && imageId != null) {
        navController.navigateTo(ITEM_DETAIL_ROUTE, Screen.Pane2, defaultNavOptions)
        Log.d(NAV_TAG, "isSinglePane $isSinglePane and imageId $imageId, so navigating to $ITEM_DETAIL_ROUTE")
    } else if (section.route != currentRoute) {
        navController.navigateTo(currentRoute, screen = Screen.Pane1, defaultNavOptions)
        Log.d(NAV_TAG, "section.route ${section.route} and currentRoute $currentRoute, so navigating to $currentRoute")
    }

    val twoPaneNavScope = this

    // Use navigation rail when dual screen (more space), otherwise use bottom navigation
    Scaffold(
        bottomBar = {
            if (!isDualScreen)
                GalleryBottomNav(navController, navDestinations, updateImageId, updateRoute)
        },
    ) { paddingValues ->
        Row(Modifier.padding(paddingValues)) {
            if (isDualScreen)
                twoPaneNavScope.GalleryNavRail(navController, navDestinations, updateImageId, updateRoute)
            Scaffold(
                topBar = { GalleryTopBar(section.route, horizontalPadding) }
            ) {
                GalleryView(
                    galleryList = section.list,
                    currentImageId = imageId,
                    onImageSelected = { id -> twoPaneNavScope.onImageSelected(id, updateImageId, navController) },
                    horizontalPadding = horizontalPadding,
                )
            }
        }
    }
}

/**
 * When an image in a gallery is selected, update the id of the currently selected image and
 * show the detail view of the item
 */
private fun TwoPaneNavScope.onImageSelected(
    id: Int,
    updateImageId: (Int?) -> Unit,
    navController: NavHostController
) {
    // Update image id
    updateImageId(id)

    // Navigate to ItemDetailView if not showing two panes
    if (isSinglePane)
        navController.navigateTo(ITEM_DETAIL_ROUTE, Screen.Pane2, defaultNavOptions)
}

/**
 * Show a grid with all of the items in the current gallery
 *
 * @param galleryList: list of the images to show in the gallery
 * @param currentImageId: id of the currently selected image
 * @param onImageSelected: action to perform when a gallery item/image is selected
 * @param horizontalPadding: amount of horizontal padding to put around the gallery grid
 */
@ExperimentalFoundationApi
@Composable
fun GalleryView(
    galleryList: List<Image>,
    currentImageId: Int?,
    onImageSelected: (Int) -> Unit,
    horizontalPadding: Dp
) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(count = NUM_COLUMNS),
        verticalArrangement = Arrangement.spacedBy(GALLERY_SPACING, Alignment.Top),
        horizontalArrangement = Arrangement.spacedBy(GALLERY_SPACING, Alignment.CenterHorizontally),
        contentPadding = PaddingValues(
            start = horizontalPadding,
            end = horizontalPadding,
            bottom = GALLERY_SPACING
        )
    ) {
        items(galleryList) { item ->
            GalleryItem(item, currentImageId, onImageSelected)
        }
    }
}

/**
 * Show the visual representation of a gallery item, and show a colored border around the item
 * if it is selected
 *
 * @param image: image associated with the item
 * @param currentImageId: id of the currently selected image
 * @param onImageSelected: action to perform when the item is selected
 */
@ExperimentalFoundationApi
@Composable
fun GalleryItem(image: Image, currentImageId: Int?, onImageSelected: (Int) -> Unit) {
    Image(
        painterResource(id = image.image),
        contentDescription = stringResource(R.string.image_description, image.name, image.id),
        modifier = Modifier
            .selectable(
                onClick = { onImageSelected(image.id) },
                selected = image.id == currentImageId,
            )
            .then(
                if (image.id == currentImageId)
                    Modifier.border(BORDER_SIZE, MaterialTheme.colors.error)
                else
                    Modifier
            ),
        contentScale = ContentScale.FillWidth
    )
}
