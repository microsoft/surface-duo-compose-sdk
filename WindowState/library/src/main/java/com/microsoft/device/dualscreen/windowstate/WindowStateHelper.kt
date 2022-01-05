/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.windowstate

import android.app.Activity
import android.graphics.Rect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowMetricsCalculator
import kotlinx.coroutines.flow.collect

@Composable
fun Activity.rememberWindowState(): WindowState {
    val activity = this
    val windowInfoRepo = WindowInfoTracker.getOrCreate(activity)

    var hasFold by remember { mutableStateOf(false) }
    var isFoldHorizontal by remember { mutableStateOf(false) }
    var foldBounds by remember { mutableStateOf(Rect()) }
    var foldState by remember { mutableStateOf(FoldState.FLAT) }
    var foldSeparates by remember { mutableStateOf(false) }
    var foldOccludes by remember { mutableStateOf(false) }

    LaunchedEffect(windowInfoRepo) {
        windowInfoRepo.windowLayoutInfo(activity).collect { newLayoutInfo ->
            hasFold = newLayoutInfo.displayFeatures.isNotEmpty()
            if (hasFold) {
                val fold = newLayoutInfo.displayFeatures.firstOrNull() as? FoldingFeature
                fold?.let {
                    isFoldHorizontal = it.orientation == FoldingFeature.Orientation.HORIZONTAL
                    foldBounds = it.bounds
                    foldState = when (it.state) {
                        FoldingFeature.State.HALF_OPENED -> FoldState.HALF_OPENED
                        else -> FoldState.FLAT
                    }
                    foldSeparates = it.isSeparating
                    foldOccludes = it.occlusionType == FoldingFeature.OcclusionType.FULL
                }
            }
        }
    }

    val config = LocalConfiguration.current
    val windowMetrics = remember(config) {
        WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this).bounds
    }

    val windowWidth = with(LocalDensity.current) { windowMetrics.width().toDp() }
    val windowHeight = with(LocalDensity.current) { windowMetrics.height().toDp() }
    val widthSizeClass = getWindowSizeClass(windowWidth)
    val heightSizeClass = getWindowSizeClass(windowHeight, Dimension.HEIGHT)

    return WindowState(
        hasFold,
        isFoldHorizontal,
        foldBounds,
        foldState,
        foldSeparates,
        foldOccludes,
        widthSizeClass,
        heightSizeClass
    )
}
