package com.microsoft.device.dualscreen.twopanelayout.twopanelayout

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.debugInspectorInfo
import com.microsoft.device.dualscreen.twopanelayout.TwoPaneScope
import com.microsoft.device.dualscreen.twopanelayout.common.LayoutWeightImpl

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
