package com.microsoft.device.dualscreen.twopanelayout.twopanelayoutnav

import android.app.Activity
import android.util.Log
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
import com.microsoft.device.dualscreen.twopanelayout.Screen
import com.microsoft.device.dualscreen.twopanelayout.TwoPaneNavScope
import com.microsoft.device.dualscreen.twopanelayout.common.LayoutWeightImpl

internal object TwoPaneNavScopeInstance : TwoPaneNavScope {
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

    override fun NavHostController.navigateTo(
        route: String,
        launchScreen: Screen,
        builder: NavOptionsBuilder.() -> Unit,
    ) {
        // Handle navOptions that affect the backstack
        val navOptions = navOptions(builder)

        // popUpTo
        navOptions.popUpToRoute?.let { targetRoute ->
            val targetEntryIndex = backStack.indexOfLast { it.route == targetRoute }

            if (targetEntryIndex != -1) {
                var numToPop = backStack.size - targetEntryIndex

                if (navOptions.isPopUpToInclusive())
                    numToPop++

                repeat(numToPop) {
                    backStack.removeLast()
                }
            }
        }

        // launchSingleTop
        if (navOptions.shouldLaunchSingleTop()) {
            backStack.removeAll { it.route == route }
        }

        // Navigate to desired destination
        if (isSinglePane) {
            navigateSinglePaneTo(route, builder)
        } else {
            when (launchScreen) {
                Screen.Pane1 -> navigatePane1To(route)
                Screen.Pane2 -> navigatePane2To(route)
            }
        }

        // Update backstack
        backStack.add(TwoPaneBackStackEntry(route, launchScreen))
    }

    override fun NavHostController.navigateBack(): Boolean {
        var finishActivity = false

        if (isSinglePane) {
            if (backStack.size > 1) {
                // Navigate up and pop backstack
                navigateSinglePaneUp()
                backStack.removeLast()
            } else {
                // Last entry in backstack, so clear and finish activity
                backStack.clear()
                finishActivity = true
            }
        } else {
            if (backStack.size > 2) {
                // Pop backstack
                val currentEntry = backStack.removeLast()

                // Check launch screen from popped entry and update the content displayed
                val launchScreen = currentEntry.launchScreen
                val prevEntry = backStack.findLast { it.launchScreen == launchScreen }
                if (prevEntry == null) {
                    Log.d(
                        "TwoPaneNavScope",
                        "Attempting to navigate up in $launchScreen, but no previous backstack entry found" +
                            "in that screen. Backstack: $backStack"
                    )
                    return false
                }

                // Navigate to the desired route in the launch screen
                when (launchScreen) {
                    Screen.Pane1 -> navigatePane1To(prevEntry.route)
                    Screen.Pane2 -> navigatePane2To(prevEntry.route)
                }
            } else {
                // Last entries in backstack, so clear and finish activity
                backStack.clear()
                finishActivity = true
            }
        }

        // Finish activity when backstack is empty
        if (finishActivity)
            (context as? Activity)?.finish()

        return true
    }

    override val currentSinglePaneDestination: String
        get() = getSinglePaneDestination()

    override val currentPane1Destination: String
        get() = getPane1Destination()

    override val currentPane2Destination: String
        get() = getPane2Destination()

    override val isSinglePane: Boolean
        get() = isSinglePaneHandler()
}
