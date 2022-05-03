package com.microsoft.device.dualscreen.twopanelayout.twopanelayoutnav

import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import com.microsoft.device.dualscreen.twopanelayout.common.LayoutWeightImpl
import com.microsoft.device.dualscreen.twopanelayout.common.Screen

@LayoutScopeMarker
@Immutable
interface TwoPaneNavScope {
    /**
     * Determines how much of the screen content will occupy
     *
     * @param weight: percentage of the screen to fill (between 0-1)
     */
    @Stable
    fun Modifier.weight(weight: Float): Modifier

    /**
     * Navigates to the given destination. In single pane mode, this changes the current destination
     * in the NavHost. In two pane mode, this updates the content in the given screen/pane.
     *
     * @param route: route of the destination to navigate to
     * @param navOptions: optional navigation options to use in single pane mode
     * @param screen: the screen (pane 1 or pane 2) in which to change content in two pane mode
     */
    @Stable
    fun NavHostController.navigateTo(
        route: String,
        navOptions: NavOptionsBuilder.() -> Unit = { },
        screen: Screen? = null
    )
}

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
        if (isSinglePaneLayout) {
            navigateSinglePaneTo(route, navOptions)
        } else {
            screen ?: throw IllegalArgumentException("Screen cannot be null when in two pane mode")

            when (screen) {
                Screen.Pane1 -> navigatePane1To(route)
                Screen.Pane2 -> navigatePane2To(route)
            }
        }
    }
}
