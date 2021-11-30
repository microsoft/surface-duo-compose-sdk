/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.twopanelayout.screenState

import android.app.Activity
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.graphics.Rect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import kotlinx.coroutines.flow.collect

const val SMALLEST_TABLET_SCREEN_WIDTH_DP = 585

@Composable
fun ConfigScreenState(onStateChange: (ScreenState) -> Unit) {
    val context = LocalContext.current
    val activity = context as Activity
    val windowInfoRep = WindowInfoTracker.getOrCreate(context)

    val smallestScreenWidthDp = LocalConfiguration.current.smallestScreenWidthDp
    val isTablet = smallestScreenWidthDp > SMALLEST_TABLET_SCREEN_WIDTH_DP
    var deviceType = if (isTablet) DeviceType.Big else DeviceType.Single
    var layoutState = if (isTablet) LayoutState.Open else LayoutState.Fold
    val screenHeight = LocalConfiguration.current.screenHeightDp * LocalDensity.current.density
    val screenWidth = LocalConfiguration.current.screenWidthDp * LocalDensity.current.density
    var orientation = orientationMappingFromScreen(LocalConfiguration.current.orientation)

    LaunchedEffect(windowInfoRep) {
        windowInfoRep.windowLayoutInfo(activity)
            .collect { newLayoutInfo ->
                var featureBounds = Rect()
                if (newLayoutInfo.displayFeatures.isNotEmpty()) {
                    val foldingFeature = newLayoutInfo.displayFeatures.first() as FoldingFeature
                    featureBounds = foldingFeature.bounds
                    layoutState = if (foldingFeature.isSeparating) LayoutState.Open else LayoutState.Fold
                    orientation = orientationMappingFromFoldingFeature(foldingFeature.orientation)
                    deviceType = DeviceType.Dual
                }

                val screenState = ScreenState(
                    deviceType = deviceType,
                    screenSize = Size(width = screenWidth, height = screenHeight),
                    hingeBounds = featureBounds,
                    orientation = orientation,
                    layoutState = layoutState
                )
                onStateChange(screenState)
            }
    }
}

private fun orientationMappingFromFoldingFeature(original: FoldingFeature.Orientation): LayoutOrientation {
    return if (original == FoldingFeature.Orientation.VERTICAL) {
        LayoutOrientation.Vertical
    } else {
        LayoutOrientation.Horizontal
    }
}

/**
 * For tablet or single-screen device
 * Portrait orientation will dual-landscape mode, which is treated as using horizontal hinge
 * Landscape orientation will be dual-portrait mode, which is treated as using vertical hinge
 */
private fun orientationMappingFromScreen(original: Int): LayoutOrientation {
    return if (original == ORIENTATION_PORTRAIT) {
        LayoutOrientation.Horizontal
    } else {
        LayoutOrientation.Vertical
    }
}
