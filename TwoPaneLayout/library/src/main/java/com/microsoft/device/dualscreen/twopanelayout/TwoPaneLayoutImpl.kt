/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.twopanelayout

import android.util.Log
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
import androidx.compose.ui.unit.dp
import com.microsoft.device.dualscreen.windowstate.WindowMode
import kotlin.math.roundToInt

@Composable
internal fun twoPaneMeasurePolicy(
    windowMode: WindowMode,
    foldSizePx: Float,
    density: Density,
    getPaneSizes: (pane1Weight: Float) -> Pair<Size, Size>,
    mockConstraints: Constraints = Constraints(0, 0, 0, 0)
): MeasurePolicy {
    return MeasurePolicy { measurables, constraints ->
        val childrenConstraints =
            if (mockConstraints == Constraints(0, 0, 0, 0))
                constraints
            else
                mockConstraints

        val twoPaneParentData = Array(measurables.size) { measurables[it].data }
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

        val pane1Weight = twoPaneParentData[0].weight / totalWeight
        val paneSizesDp = getPaneSizes(pane1Weight)
        var pane1SizePx: Size
        var pane2SizePx: Size
        with(density) {
            val pane1WidthPx = paneSizesDp.first.width.dp.toPx()
            val pane1HeightPx = paneSizesDp.first.height.dp.toPx()

            val pane2WidthPx = paneSizesDp.second.width.dp.toPx()
            val pane2HeightPx = paneSizesDp.second.height.dp.toPx()
            pane1SizePx = Size(pane1WidthPx, pane1HeightPx)
            pane2SizePx = Size(pane2WidthPx, pane2HeightPx)
        }

        val paneSizesPx = arrayOf(pane1SizePx, pane2SizePx)
        val placeables = measureTwoPanes(childrenConstraints, paneSizesPx, measurables)

        layout(childrenConstraints.maxWidth, childrenConstraints.maxHeight) {
            placeTwoPanes(
                windowMode = windowMode,
                placeables = placeables,
                foldSizePx = foldSizePx,
            )
        }
    }
}

/**
 * To measure the two panes for dual-screen/foldable/large-screen with and without weight
 */
private fun measureTwoPanes(
    constraints: Constraints,
    paneSizesPx: Array<Size>,
    measurables: List<Measurable>,
): List<Placeable> {
    val placeables = emptyList<Placeable>().toMutableList()
    val minWidth = constraints.minWidth
    val maxWidth = constraints.maxWidth
    val minHeight = constraints.minHeight
    val maxHeight = constraints.maxHeight

    for (i in measurables.indices) {
        val paneWidth = paneSizesPx[i].width.roundToInt()
        val paneHeight = paneSizesPx[i].height.roundToInt()
        val childConstraints = Constraints(
            minWidth = minWidth.coerceAtMost(paneWidth),
            minHeight = minHeight.coerceAtMost(paneHeight),
            maxWidth = maxWidth.coerceAtMost(paneWidth),
            maxHeight = maxHeight.coerceAtMost(paneHeight)
        )

        val placeable = measurables[i].measure(childConstraints)
        placeables.add(placeable)
    }
    return placeables
}

private fun Placeable.PlacementScope.placeTwoPanes(
    windowMode: WindowMode,
    placeables: List<Placeable>,
    foldSizePx: Float,
) {
    when (windowMode) {
        WindowMode.DUAL_PORTRAIT -> {
            placeables[0].place(x = 0, y = 0)
            placeables[1].place(x = (placeables[0].width + foldSizePx).roundToInt(), y = 0)
        }
        WindowMode.DUAL_LANDSCAPE -> {
            placeables[0].place(x = 0, y = 0)
            placeables[1].place(x = 0, y = (placeables[0].height + foldSizePx).roundToInt())
        }
        else -> {
            Log.e(
                "TwoPaneLayoutImpl",
                "Error: using TwoPaneContainer when in a single pane window mode ($windowMode)"
            )
        }
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
