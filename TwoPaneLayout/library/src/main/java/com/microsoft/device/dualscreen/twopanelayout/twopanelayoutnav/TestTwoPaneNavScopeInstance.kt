package com.microsoft.device.dualscreen.twopanelayout.twopanelayoutnav

import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import com.microsoft.device.dualscreen.twopanelayout.Screen
import com.microsoft.device.dualscreen.twopanelayout.TwoPaneNavScope

object TestTwoPaneNavScopeInstance : TwoPaneNavScope {
    override fun Modifier.weight(weight: Float): Modifier {
        return this.then(Modifier)
    }

    override fun NavHostController.navigateTo(
        route: String,
        screen: Screen,
        navOptions: NavOptionsBuilder.() -> Unit
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

    // Backing fields for test scope instance
    private var singlePaneDestination = ""
    private var pane1Destination = ""
    private var pane2Destination = ""
    private var isSinglePaneMode = true

    fun setSinglePaneDestination(route: String) {
        singlePaneDestination = route
    }

    fun setPane1Destination(route: String) {
        pane1Destination = route
    }

    fun setPane2Destination(route: String) {
        pane2Destination = route
    }

    fun setIsSinglePane(value: Boolean) {
        isSinglePaneMode = value
    }
}
