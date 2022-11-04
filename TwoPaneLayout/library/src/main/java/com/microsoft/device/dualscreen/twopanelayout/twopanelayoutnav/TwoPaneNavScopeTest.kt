package com.microsoft.device.dualscreen.twopanelayout.twopanelayoutnav

import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import com.microsoft.device.dualscreen.twopanelayout.Screen
import com.microsoft.device.dualscreen.twopanelayout.TwoPaneNavScope

class TwoPaneNavScopeTest(
    currentSinglePaneDestination: String = "",
    currentPane1Destination: String = "",
    currentPane2Destination: String = "",
    isSinglePane: Boolean = true
) : TwoPaneNavScope {
    // Backing fields for test scope instance
    private var singlePaneDestination = currentSinglePaneDestination
    private var pane1Destination = currentPane1Destination
    private var pane2Destination = currentPane2Destination
    private var isSinglePaneMode = isSinglePane

    override fun Modifier.weight(weight: Float): Modifier {
        return this.then(Modifier)
    }

    override fun NavHostController.navigateTo(
        route: String,
        launchScreen: Screen,
        builder: NavOptionsBuilder.() -> Unit
    ) {
    }

    override fun NavHostController.navigateUpTo(
        route: String?,
        launchScreen: Screen
    ) {
    }

    override val currentSinglePaneDestination: String
        get() = singlePaneDestination

    override val currentPane1Destination: String
        get() = pane1Destination

    override val currentPane2Destination: String
        get() = pane2Destination

    override val isSinglePane: Boolean
        get() = isSinglePaneMode
}
