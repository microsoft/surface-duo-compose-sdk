package com.microsoft.device.dualscreen.twopanelayout

import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder

@LayoutScopeMarker
@Immutable
interface TwoPaneScope {
    @Stable
    fun Modifier.weight(weight: Float): Modifier

    @Stable
    fun navigateToPane1()

    @Stable
    fun navigateToPane2()

    @Stable
    fun isPane1Shown(): Boolean
}

@LayoutScopeMarker
@Immutable
interface TwoPaneNavScope {
    @Stable
    fun Modifier.weight(weight: Float): Modifier

    @Stable
    fun NavHostController.navigateTo(
        route: String,
        navOptions: NavOptionsBuilder.() -> Unit = { },
        screen: Screen? = null
    )
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

    /**
     * Navigation to the first pane in the single-pane mode
     */
    @Stable
    override fun navigateToPane1() {
        navigateToPane1Handler()
    }

    /**
     * Navigation to the second pane in the single-pane mode
     */
    @Stable
    override fun navigateToPane2() {
        navigateToPane2Handler()
    }

    /**
     * Return whether pane 1 is shown currently, otherwise pane 2
     */
    override fun isPane1Shown(): Boolean {
        return isPane1ShownHandler()
    }
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
