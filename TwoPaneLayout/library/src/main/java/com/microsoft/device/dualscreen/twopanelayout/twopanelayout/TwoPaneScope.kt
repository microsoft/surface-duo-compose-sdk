package com.microsoft.device.dualscreen.twopanelayout.twopanelayout

import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.debugInspectorInfo
import com.microsoft.device.dualscreen.twopanelayout.common.LayoutWeightImpl

@LayoutScopeMarker
@Immutable
interface TwoPaneScope {
    /**
     * Determines how much of the screen content will occupy
     *
     * @param weight: percentage of the screen to fill (between 0-1)
     */
    @Stable
    fun Modifier.weight(weight: Float): Modifier

    /**
     * Navigates to the content in the first pane in single pane mode, otherwise does nothing in
     * two pane mode
     */
    @Stable
    fun navigateToPane1()

    /**
     * Navigates to the content in the second pane in single pane mode, otherwise does nothing in
     * two pane mode
     */
    @Stable
    fun navigateToPane2()

    /**
     * Return true when pane 1 is shown in single pane mode, false when pane 2 is shown
     */
    @Stable
    fun isPane1Shown(): Boolean
}

internal object TwoPaneScopeInstance : TwoPaneScope {
    @Stable
    override fun Modifier.weight(weight: Float): Modifier {
        require(weight > 0.0) { "invalid weight $weight; must be greater than zero" }
        return this.then(
            LayoutWeightImpl(
                weight = weight,
                inspectorInfo = debugInspectorInfo {
                    name = "weight"
                    value = weight
                    properties["weight"] = weight
                }
            )
        )
    }

    @Stable
    override fun navigateToPane1() {
        navigateToPane1Handler()
    }

    @Stable
    override fun navigateToPane2() {
        navigateToPane2Handler()
    }

    override fun isPane1Shown(): Boolean {
        return isPane1ShownHandler()
    }
}
