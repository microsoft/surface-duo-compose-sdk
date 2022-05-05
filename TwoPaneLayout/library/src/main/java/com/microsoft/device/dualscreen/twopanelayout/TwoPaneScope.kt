package com.microsoft.device.dualscreen.twopanelayout

import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier

@LayoutScopeMarker
@Immutable
interface TwoPaneScope {
    /**
     * Determines how much of the screen content will occupy
     *
     * @param weight: percentage of the screen to fill (between 0-1)
     */
    @Stable
    fun Modifier.weight(
        weight: Float,
    ): Modifier

    /**
     * Navigates to the content in the first pane in single pane mode, otherwise does nothing in two pane mode
     */
    @Stable
    fun navigateToPane1()

    /**
     * Navigates to the content in the second pane in single pane mode, otherwise does nothing in two pane mode
     */
    @Stable
    fun navigateToPane2()

    /**
     * Returns true when pane 1 is shown currently in single pane mode, otherwise false when pane 2 is shown
     */
    @Stable
    fun isPane1Shown(): Boolean
}
