package com.microsoft.device.dualscreen.twopanelayout.twopanelayout

import androidx.compose.ui.Modifier
import com.microsoft.device.dualscreen.twopanelayout.TwoPaneNavScope
import com.microsoft.device.dualscreen.twopanelayout.TwoPaneScope

class TwoPaneScopeTest(
    currentSinglePaneDestination: String  = "",
    isSinglePane: Boolean = true
) : TwoPaneScope {
    // Backing fields for test scope instance
    private var singlePaneDestination = currentSinglePaneDestination
    private var isSinglePaneMode = isSinglePane

    override fun Modifier.weight(weight: Float): Modifier {
        return this.then(Modifier)
    }

    override fun navigateToPane1() {}

    override fun navigateToPane2() {}

    override val currentSinglePaneDestination: String
        get() = singlePaneDestination

    override val isSinglePane: Boolean
        get() = isSinglePaneMode

}
