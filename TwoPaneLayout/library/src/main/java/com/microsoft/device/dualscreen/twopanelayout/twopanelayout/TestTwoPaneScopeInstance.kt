package com.microsoft.device.dualscreen.twopanelayout.twopanelayout

import androidx.compose.ui.Modifier
import com.microsoft.device.dualscreen.twopanelayout.TwoPaneScope

object TestTwoPaneScopeInstance : TwoPaneScope {
    override fun Modifier.weight(weight: Float): Modifier {
        return this.then(Modifier)
    }

    override fun navigateToPane1() {}

    override fun navigateToPane2() {}

    override val currentSinglePaneDestination: String
        get() = singlePaneDestination

    override val isSinglePane: Boolean
        get() = isSinglePaneMode

    // Backing fields for test scope instance
    private var singlePaneDestination = ""
    private var isSinglePaneMode = true

    fun setSinglePaneDestination(route: String) {
        singlePaneDestination = route
    }

    fun setIsSinglePane(value: Boolean) {
        isSinglePaneMode = value
    }
}
