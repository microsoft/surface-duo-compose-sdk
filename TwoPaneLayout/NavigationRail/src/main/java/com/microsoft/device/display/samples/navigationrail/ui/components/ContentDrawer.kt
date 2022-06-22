/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.navigationrail.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffoldDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.SwipeableState
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.height
import com.microsoft.device.display.samples.navigationrail.R

private const val CONTENT_HORIZ_PADDING_PERCENT = 0.06f
private val DrawerShape = RoundedCornerShape(
    topStart = 25.dp,
    topEnd = 25.dp,
    bottomStart = 0.dp,
    bottomEnd = 0.dp
)
val DrawerStateKey = SemanticsPropertyKey<DrawerState>("DrawerStateKey")
var SemanticsPropertyReceiver.drawerState by DrawerStateKey

enum class DrawerState { Collapsed, Expanded }

/**
 * Reference: BottomDrawer component code
 *
 * Custom drawer (bottom aligned) with a rounded corner shape that swipes between a collapsed
 * "peek" view and a more expanded view that displays all of the content
 *
 * Supports foldable displays by splitting the "peekContent" and "hiddenContent" around an occluding
 * fold
 *
 * @param modifier: optional Modifier to be applied to the layout
 * @param expandedHeightPct: height of the drawer when expanded, expressed as percentage of maximum possible
 * height (must be > 0, <= 1)
 * @param collapsedHeightPct: height of the drawer when collapsed, expressed as percentage of maximum possible
 * height (must be > 0, <= 1)
 * @param foldIsOccluding: optional param for foldable support, indicates whether there is a hinge
 * that occludes content in the current layout
 * @param foldBoundsDp: optional param for foldable support, indicates the coordinates of the boundary
 * of a fold
 * @param windowHeightDp: optional param for foldable support, indicates the full height of the window
 * in which a fold and the content drawer are being displayed
 * @param foldBottomPaddingDp: optional param for foldable support, will be added as padding below the fold to
 * make content more accessible to users
 * @param hiddenContent: the content that will only be shown when the drawer is expanded
 * @param peekContent: the content that will be shown even when the drawer is collapsed
 */
@ExperimentalMaterialApi
@Composable
fun BoxWithConstraintsScope.ContentDrawer(
    modifier: Modifier = Modifier,
    expandedHeightPct: Float,
    collapsedHeightPct: Float,
    foldIsOccluding: Boolean = false,
    foldBoundsDp: DpRect,
    windowHeightDp: Dp = 0.dp,
    foldBottomPaddingDp: Dp = 0.dp,
    hiddenContent: @Composable ColumnScope.() -> Unit,
    peekContent: @Composable ColumnScope.() -> Unit,
) {
    // Calculate drawer y coordinates for the collapsed and expanded states - pixels
    if (!expandedHeightPct.isInPctRange() || !collapsedHeightPct.isInPctRange())
        throw IllegalArgumentException("expandedHeightPct $expandedHeightPct or collapsedHeightPct $collapsedHeightPct is not in range (0, 1]")
    val fullHeight = constraints.maxHeight.toFloat()
    val expandHeightPx = expandedHeightPct * fullHeight
    val collapseHeightPx = collapsedHeightPct * fullHeight
    val swipeHeightPx = expandHeightPx - collapseHeightPx

    // Set up swipeable modifier fields
    val swipeableState = rememberSwipeableState(initialValue = DrawerState.Collapsed)
    val anchors = mapOf(swipeHeightPx to DrawerState.Collapsed, 0f to DrawerState.Expanded)

    // Calculate the height of each drawer component (top content, fold, bottom content) - dp
    val expandHeightDp = with(LocalDensity.current) { expandHeightPx.toDp() }
    val collapseHeightDp = with(LocalDensity.current) { collapseHeightPx.toDp() }
    val foldSizeDp = foldBoundsDp.height
    val bottomContentMaxHeightDp = windowHeightDp - foldBoundsDp.bottom
    val topContentMaxHeightDp: Dp = if (foldIsOccluding) {
        expandHeightDp - foldSizeDp - bottomContentMaxHeightDp
    } else {
        collapseHeightDp
    }

    BoxWithConstraints(
        modifier = modifier
            .align(Alignment.BottomCenter)
            .swipeable(swipeableState, anchors, Orientation.Vertical)
            .semantics { this.drawerState = swipeableState.currentValue }
            .testTag(stringResource(R.string.content_drawer)),
        contentAlignment = Alignment.TopStart,
    ) {
        // Check if a spacer needs to be included to render content around an occluding hinge
        val minSpacerHeight = calculateSpacerHeight(
            foldIsOccluding,
            swipeableState,
            foldSizeDp.value + foldBottomPaddingDp.value
        ).toInt().dp

        // Calculate drawer height in dp based on swipe state
        val swipeOffsetDp = with(LocalDensity.current) { swipeableState.offset.value.toDp() }
        val drawerHeight = expandHeightDp - swipeOffsetDp

        Surface(
            modifier = Modifier
                .heightIn(collapseHeightDp, expandHeightDp)
                .height(drawerHeight)
                .clip(DrawerShape)
                .background(MaterialTheme.colors.surface),
            elevation = BottomSheetScaffoldDefaults.SheetElevation
        ) {
            // Calculate horizontal padding for drawer content
            val paddingPx = CONTENT_HORIZ_PADDING_PERCENT * constraints.maxWidth.toFloat()
            val paddingDp = with(LocalDensity.current) { paddingPx.toDp() }

            val fillWidth = Modifier.fillMaxWidth()

            Column(
                modifier = Modifier.padding(horizontal = paddingDp),
            ) {
                Column(fillWidth.requiredHeight(topContentMaxHeightDp)) { peekContent() }
                Spacer(Modifier.requiredHeight(minSpacerHeight))
                hiddenContent()
            }
        }
    }
}

/**
 * Helper method to calculate the animated height of the spacer used for foldable support. Height
 * is progressively increased or decreased based on the swipe state.
 *
 * @param foldIsOccluding: whether or not a fold is present and occluding content
 * @param swipeableState: swipeable state of the component that contains a spacer
 * @param fullHeight: the desired full height of the spacer when the parent component has been swiped
 * to the expanded state
 *
 * @return the height of the spacer for the current swipe progress
 */
@ExperimentalMaterialApi
private fun calculateSpacerHeight(
    foldIsOccluding: Boolean,
    swipeableState: SwipeableState<DrawerState>,
    fullHeight: Float
): Float {
    if (!foldIsOccluding)
        return 0f

    val isExpanding = swipeableState.progress.to == DrawerState.Expanded
    val progressHeight = (fullHeight * swipeableState.progress.fraction)

    return if (isExpanding) progressHeight else fullHeight - progressHeight
}

/**
 * Helper method that checks if a percentage is valid
 */
private fun Float.isInPctRange(): Boolean {
    return this > 0f && this <= 1f
}
