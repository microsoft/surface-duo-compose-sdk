/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.twopanelayout.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.InspectorValueInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import com.microsoft.device.dualscreen.windowstate.WindowMode
import kotlin.math.roundToInt

@Composable
internal fun twoPaneMeasurePolicy(
    windowMode: WindowMode,
    isSeparating: Boolean,
    paneSizes: Array<Size>,
    mockConstraints: Constraints = Constraints(0, 0, 0, 0)
): MeasurePolicy {
    return MeasurePolicy { measurables, constraints ->
        val childrenConstraints =
            if (mockConstraints == Constraints(0, 0, 0, 0))
                constraints
            else
                mockConstraints

        val twoPaneParentData = Array(measurables.size) { measurables[it].data }
        val placeables: List<Placeable>
        var totalWeight = 0f
        var maxWeight = 0f

        val childrenCount = measurables.count()
        require(childrenCount == 2) { "TwoPaneLayout requires 2 child elements in the two pane mode" }

        for (i in measurables.indices) {
            val parentData = twoPaneParentData[i]
            val weight = parentData.weight
            if (weight > 0f) {
                totalWeight += weight
                maxWeight = weight.coerceAtLeast(maxWeight)
            }
        }

        // there is no weight or two weights are equal
        val hasEqualWeight: Boolean = maxWeight == 0f || maxWeight * 2 == totalWeight
        // shows two panes equally when the foldingFeature is separating or the weights are equal
        val shouldLayoutEqually = isSeparating || hasEqualWeight

        placeables = if (shouldLayoutEqually) {
            measureTwoPaneEqually(
                constraints = childrenConstraints,
                paneSizes = paneSizes,
                measurables = measurables
            )
        } else { // the foldingFeature is not separating and the weights are not equal
            measureTwoPaneProportionally(
                constraints = childrenConstraints,
                measurables = measurables,
                totalWeight = totalWeight,
                windowMode = windowMode,
                twoPaneParentData = twoPaneParentData
            )
        }

        if (shouldLayoutEqually) {
            layout(childrenConstraints.maxWidth, childrenConstraints.maxHeight) {
                placeables.forEachIndexed { index, placeable ->
                    placeTwoPaneEqually(
                        windowMode = windowMode,
                        placeable = placeable,
                        index = index,
                        lastPaneSize = paneSizes[1],
                        constraints = childrenConstraints
                    )
                }
            }
        } else {
            layout(childrenConstraints.maxWidth, childrenConstraints.maxHeight) {
                placeables.forEachIndexed { index, placeable ->
                    placeTwoPaneProportionally(
                        windowMode = windowMode,
                        placeable = placeable,
                        index = index,
                        twoPaneParentData = twoPaneParentData,
                        constraints = childrenConstraints,
                        totalWeight = totalWeight
                    )
                }
            }
        }
    }
}

/**
 * to measure the two panes for dual-screen/foldable/large-screen without weight,
 * or with two equal weight
 */
private fun measureTwoPaneEqually(
    constraints: Constraints,
    paneSizes: Array<Size>,
    measurables: List<Measurable>
): List<Placeable> {
    val placeables = emptyList<Placeable>().toMutableList()

    for (i in measurables.indices) {
        val paneWidth = paneSizes[i].width.roundToInt()
        val paneHeight = paneSizes[i].height.roundToInt()

        val childConstraints = Constraints(
            minWidth = constraints.minWidth.coerceAtMost(paneWidth),
            minHeight = constraints.minHeight.coerceAtMost(paneHeight),
            maxWidth = constraints.maxWidth.coerceAtMost(paneWidth),
            maxHeight = constraints.maxHeight.coerceAtMost(paneHeight)
        )

        placeables.add(measurables[i].measure(childConstraints))
    }

    return placeables
}

/**
 * to measure the pane for dual-screen with two non-equal weight
 */
private fun measureTwoPaneProportionally(
    constraints: Constraints,
    measurables: List<Measurable>,
    totalWeight: Float,
    windowMode: WindowMode,
    twoPaneParentData: Array<TwoPaneParentData?>
): List<Placeable> {
    val minWidth = constraints.minWidth
    val minHeight = constraints.minHeight
    val maxWidth = constraints.maxWidth
    val maxHeight = constraints.maxHeight

    val placeables = emptyList<Placeable>().toMutableList()
    for (i in measurables.indices) {
        val parentData = twoPaneParentData[i]
        val weight = parentData.weight
        var childConstraints: Constraints
        val ratio = weight / totalWeight
        childConstraints = when (windowMode) {
            WindowMode.DUAL_PORTRAIT -> {
                Constraints(
                    minWidth = (minWidth * ratio).roundToInt(),
                    minHeight = minHeight,
                    maxWidth = (maxWidth * ratio).roundToInt(),
                    maxHeight = maxHeight
                )
            }
            WindowMode.DUAL_LANDSCAPE -> {
                Constraints(
                    minWidth = minWidth,
                    minHeight = (minHeight * ratio).roundToInt(),
                    maxWidth = maxWidth,
                    maxHeight = (maxHeight * ratio).roundToInt()
                )
            }
            else -> throw IllegalStateException("[measureTwoPaneProportionally] Error: single pane window mode ($windowMode) found inside TwoPaneContainer")
        }

        val placeable = measurables[i].measure(childConstraints)

        placeables.add(placeable)
    }
    return placeables
}

private fun Placeable.PlacementScope.placeTwoPaneEqually(
    windowMode: WindowMode,
    placeable: Placeable,
    index: Int,
    lastPaneSize: Size,
    constraints: Constraints
) {
    when (windowMode) {
        WindowMode.DUAL_PORTRAIT -> {
            var xPosition = 0 // for the first pane
            if (index != 0) { // for the second pane
                val lastPaneWidth = lastPaneSize.width.roundToInt()
                val firstPaneWidth = constraints.maxWidth - lastPaneWidth
                xPosition += firstPaneWidth
            }
            placeable.place(x = xPosition, y = 0)
        }
        WindowMode.DUAL_LANDSCAPE -> {
            var yPosition = 0
            if (index != 0) {
                val lastPaneHeight = lastPaneSize.height.roundToInt()
                val firstPaneHeight = constraints.maxHeight - lastPaneHeight
                yPosition += firstPaneHeight
            }
            placeable.place(x = 0, y = yPosition)
        }
        else -> throw IllegalStateException("[placeTwoPaneEqually] Error: single pane window mode ($windowMode) found inside TwoPaneContainer")
    }
}

private fun Placeable.PlacementScope.placeTwoPaneProportionally(
    windowMode: WindowMode,
    placeable: Placeable,
    index: Int,
    twoPaneParentData: Array<TwoPaneParentData?>,
    constraints: Constraints,
    totalWeight: Float
) {
    when (windowMode) {
        WindowMode.DUAL_PORTRAIT -> {
            var xPosition = 0 // for the first pane
            if (index != 0) { // for the second pane
                val parentData = twoPaneParentData[index]
                val weight = parentData.weight
                val ratio = 1f - (weight / totalWeight)
                val firstPaneWidth = (constraints.maxWidth * ratio).roundToInt()
                xPosition += firstPaneWidth
            }
            placeable.place(x = xPosition, y = 0)
        }
        WindowMode.DUAL_LANDSCAPE -> {
            var yPosition = 0
            if (index != 0) {
                val parentData = twoPaneParentData[index]
                val weight = parentData.weight
                val ratio = 1f - (weight / totalWeight)
                val firstPaneHeight = (constraints.maxHeight * ratio).roundToInt()
                yPosition += firstPaneHeight
            }
            placeable.place(x = 0, y = yPosition)
        }
        else -> throw IllegalStateException("[placeTwoPaneProportionally] Error: single pane window mode ($windowMode) found inside TwoPaneContainer")
    }
}

private val IntrinsicMeasurable.data: TwoPaneParentData?
    get() = parentData as? TwoPaneParentData

private val TwoPaneParentData?.weight: Float
    get() = this?.weight ?: 1f // set weight as 1 by default to avoid the unintentional single pane

/**
 * Parent data associated with children.
 */
internal data class TwoPaneParentData(
    var weight: Float = 1f
)

internal class LayoutWeightImpl(
    val weight: Float,
    inspectorInfo: InspectorInfo.() -> Unit
) : ParentDataModifier, InspectorValueInfo(inspectorInfo) {
    override fun Density.modifyParentData(parentData: Any?) =
        ((parentData as? TwoPaneParentData) ?: TwoPaneParentData()).also {
            it.weight = weight
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherModifier = other as? LayoutWeightImpl ?: return false
        return weight != otherModifier.weight
    }

    override fun hashCode(): Int {
        var result = weight.hashCode()
        result *= 31
        return result
    }

    override fun toString(): String =
        "LayoutWeightImpl(weight=$weight)"
}
