/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.navigationrail.ui.view

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.navigation.NavHostController
import com.microsoft.device.display.samples.navigationrail.R
import com.microsoft.device.display.samples.navigationrail.models.DataProvider
import com.microsoft.device.display.samples.navigationrail.models.Image
import com.microsoft.device.display.samples.navigationrail.ui.components.ItemTopBar
import com.microsoft.device.dualscreen.twopanelayout.Destination
import com.microsoft.device.dualscreen.twopanelayout.Screen
import com.microsoft.device.dualscreen.twopanelayout.TwoPaneNavScope

@ExperimentalMaterialApi
@ExperimentalUnitApi
@Composable
fun itemDetailDestination(
    isDualPortrait: Boolean,
    isDualLandscape: Boolean,
    foldIsOccluding: Boolean,
    foldBoundsDp: DpRect,
    windowHeight: Dp,
    imageId: Int?,
    updateImageId: (Int?) -> Unit,
    currentRoute: String,
    navController: NavHostController
): Destination {
    return Destination(ITEM_DETAIL_ROUTE) {
        ItemDetailViewWithTopBar(
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
    }
}

@ExperimentalMaterialApi
@ExperimentalUnitApi
@Composable
fun TwoPaneNavScope.ItemDetailViewWithTopBar(
    isDualPortrait: Boolean,
    isDualLandscape: Boolean,
    foldIsOccluding: Boolean,
    foldBoundsDp: DpRect,
    windowHeight: Dp,
    imageId: Int?,
    updateImageId: (Int?) -> Unit,
    currentRoute: String,
    navController: NavHostController
) {
    // Make sure the current item detail should be shown
    if (isSinglePane && imageId == null) {
        navController.navigateTo(currentRoute, Screen.Pane1, itemDetailNavOptions) // REVISIT
        Log.d(NAV_TAG, "isSinglePane $isSinglePane and imageId $imageId, so navigating to $currentRoute")
    }

    // Retrieve selected image information
    val selectedImage = imageId?.let { DataProvider.getImage(imageId) }

    // Set up back press action to return to the gallery and clear image selection
    val onBackPressed = {
        updateImageId(null)
        //navController.navigateTo(currentRoute, Screen.Pane1, defaultNavOptions) // REVISIT
        navController.navigateUp()
        Log.d(NAV_TAG, "onBackPressed in ItemDetailView, so navigating up")
        logBackQueue(navController)
    }
    BackHandler(enabled = isSinglePane) { onBackPressed() }

    ItemDetailView(
        isDualPortrait = isDualPortrait,
        isDualLandscape = isDualLandscape,
        foldIsOccluding = foldIsOccluding,
        foldBoundsDp = foldBoundsDp,
        windowHeight = windowHeight,
        selectedImage = selectedImage,
        currentRoute = currentRoute,
        navController = navController
    )
    // If only one pane is being displayed, show a "back" icon
    if (!isDualPortrait) {
        ItemTopBar(
            onClick = { onBackPressed() }
        )
    }
}

/**
 * Show the image and details for the selected gallery item. If no item is selected, show
 * a placeholder view explaining how to open the detail view.
 *
 * @param isDualPortrait: true if device is in dual portrait mode
 * @param isDualLandscape: true if device is in dual landscape mode
 * @param foldIsOccluding: true if a fold is present and it occludes content, false otherwise
 * @param foldBoundsDp: the bounds of a fold in the form of an Android Rect
 * @param windowHeight: full height in dp of the window this view is being shown in
 * @param selectedImage: currently selected image
 * @param currentRoute: current route in gallery NavHost
 */
@ExperimentalUnitApi
@ExperimentalMaterialApi
@Composable
fun TwoPaneNavScope.ItemDetailView(
    isDualPortrait: Boolean,
    isDualLandscape: Boolean,
    foldIsOccluding: Boolean,
    foldBoundsDp: DpRect,
    windowHeight: Dp,
    selectedImage: Image? = null,
    currentRoute: String,
    navController: NavHostController,
) {
    // Find current gallery
    val gallerySection = navDestinations.find { it.route == currentRoute }

    // If no images are selected, show "select image" message or navigate back to gallery view
    if (selectedImage == null) {
        if (isSinglePane) {
            navController.navigateTo(currentRoute, Screen.Pane1, itemDetailNavOptions) // REVISIT
            Log.d(NAV_TAG, "Null image id inside ItemDetailView, so navigating to $currentRoute")
        }
        PlaceholderView(gallerySection)
        return
    }

    // Show the image at the top and the details drawer at the bottom
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
    ) {
        ItemImage(Modifier.align(Alignment.TopCenter), selectedImage)
        ItemDetailsDrawer(
            image = selectedImage,
            isDualLandscape = isDualLandscape,
            isDualPortrait = isDualPortrait,
            foldIsOccluding = foldIsOccluding,
            foldBoundsDp = foldBoundsDp,
            windowHeight = windowHeight,
            gallerySection = gallerySection,
        )
    }
}

@Composable
private fun ItemImage(modifier: Modifier, image: Image) {
    Image(
        painter = painterResource(id = image.image),
        contentDescription = stringResource(R.string.image_description, image.name, image.id),
        modifier = modifier.fillMaxWidth(),
        contentScale = ContentScale.FillWidth,
    )
}
