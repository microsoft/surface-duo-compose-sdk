/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.twopanelayout

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
import com.microsoft.device.dualscreen.twopanelayout.screenState.LayoutOrientation
import kotlin.math.roundToInt

@Composable
internal fun twoPaneMeasurePolicy(
    orientation: LayoutOrientation,
    paneSize: Size,
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

        // no weight or equal weight
        placeables = if (maxWeight == 0f || maxWeight * 2 == totalWeight) {
            measureTwoPaneEqually(
                constraints = childrenConstraints,
                paneSize = paneSize,
                measurables = measurables
            )
        } else {
            measureTwoPaneProportionally(
                constraints = childrenConstraints,
                measurables = measurables,
                totalWeight = totalWeight,
                orientation = orientation,
                twoPaneParentData = twoPaneParentData
            )
        }

        if (maxWeight == 0f || (maxWeight * 2 == totalWeight)) { // no weight will be layout equally by default
            layout(childrenConstraints.maxWidth, childrenConstraints.maxHeight) {
                placeables.forEachIndexed { index, placeable ->
                    placeTwoPaneEqually(
                        orientation = orientation,
                        placeable = placeable,
                        index = index,
                        paneSize = paneSize,
                        constraints = childrenConstraints
                    )
                }
            }
        } else { // two panes with different weight
            layout(childrenConstraints.maxWidth, childrenConstraints.maxHeight) {
                placeables.forEachIndexed { index, placeable ->
                    placeTwoPaneProportionally(
                        orientation = orientation,
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

/*
 * to measure the two panes for dual-screen/foldable/large-screen without weight,
 * or with two equal weight
 */
private fun measureTwoPaneEqually(
    constraints: Constraints,
    paneSize: Size,
    measurables: List<Measurable>
): List<Placeable> {
    val paneWidth = paneSize.width.toInt()
    val paneHeight = paneSize.height.toInt()
    val childConstraints = Constraints(
        minWidth = constraints.minWidth.coerceAtMost(paneWidth),
        minHeight = constraints.minHeight.coerceAtMost(paneHeight),
        maxWidth = constraints.maxWidth.coerceAtMost(paneWidth),
        maxHeight = constraints.maxHeight.coerceAtMost(paneHeight)
    )
    return measurables.map { it.measure(childConstraints) }
}

/*
 * to measure the pane for dual-screen with two non-equal weight
 */
private fun measureTwoPaneProportionally(
    constraints: Constraints,
    measurables: List<Measurable>,
    totalWeight: Float,
    orientation: LayoutOrientation,
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
        childConstraints = if (orientation == LayoutOrientation.Vertical) {
            Constraints(
                minWidth = (minWidth * ratio).roundToInt(),
                minHeight = minHeight,
                maxWidth = (maxWidth * ratio).roundToInt(),
                maxHeight = maxHeight
            )
        } else {
            Constraints(
                minWidth = minWidth,
                minHeight = (minHeight * ratio).roundToInt(),
                maxWidth = maxWidth,
                maxHeight = (maxHeight * ratio).roundToInt()
            )
        }

        val placeable = measurables[i].measure(childConstraints)
        placeables.add(placeable)
    }
    return placeables
}

private fun Placeable.PlacementScope.placeTwoPaneEqually(
    orientation: LayoutOrientation,
    placeable: Placeable,
    index: Int,
    paneSize: Size,
    constraints: Constraints
) {
    if (orientation == LayoutOrientation.Vertical) {
        var xPosition = 0 // for the first pane
        if (index != 0) { // for the second pane
            val lastPaneWidth = paneSize.width.toInt()
            val firstPaneWidth = constraints.maxWidth - lastPaneWidth
            xPosition += firstPaneWidth
        }
        placeable.place(x = xPosition, y = 0)
    } else {
        var yPosition = 0
        if (index != 0) {
            val lastPaneHeight = paneSize.height.toInt()
            val firstPaneHeight = constraints.maxHeight - lastPaneHeight
            yPosition += firstPaneHeight
        }
        placeable.place(x = 0, y = yPosition)
    }
}

private fun Placeable.PlacementScope.placeTwoPaneProportionally(
    orientation: LayoutOrientation,
    placeable: Placeable,
    index: Int,
    twoPaneParentData: Array<TwoPaneParentData?>,
    constraints: Constraints,
    totalWeight: Float
) {
    if (orientation == LayoutOrientation.Vertical) {
        var xPosition = 0 // for the first pane
        if (index != 0) { // for the second pane
            val parentData = twoPaneParentData[index]
            val weight = parentData.weight
            val ratio = 1f - (weight / totalWeight)
            val firstPaneWidth = (constraints.maxWidth * ratio).roundToInt()
            xPosition += firstPaneWidth
        }
        placeable.place(x = xPosition, y = 0)
    } else {
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
