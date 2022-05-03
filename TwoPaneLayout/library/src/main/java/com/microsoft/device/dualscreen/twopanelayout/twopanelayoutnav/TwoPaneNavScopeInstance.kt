package com.microsoft.device.dualscreen.twopanelayout.twopanelayoutnav

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import com.microsoft.device.dualscreen.twopanelayout.TwoPaneNavScope
import com.microsoft.device.dualscreen.twopanelayout.common.LayoutWeightImpl
import com.microsoft.device.dualscreen.twopanelayout.Screen

internal object TwoPaneNavScopeInstance : TwoPaneNavScope {
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
    override fun NavHostController.navigateTo(
        route: String,
        navOptions: NavOptionsBuilder.() -> Unit,
        screen: Screen?
    ) {
        if (isSinglePane()) {
            navigateSinglePaneTo(route, navOptions)
        } else {
            screen ?: throw IllegalArgumentException("Screen cannot be null when in two pane mode")

            when (screen) {
                Screen.Pane1 -> navigatePane1To(route)
                Screen.Pane2 -> navigatePane2To(route)
            }
        }
    }

    override val currentSinglePaneDestination: String
        get() = getSinglePaneDestination()

    override val currentPane1Destination: String
        get() = getPane1Destination()

    override val currentPane2Destination: String
        get() = getPane2Destination()
}
