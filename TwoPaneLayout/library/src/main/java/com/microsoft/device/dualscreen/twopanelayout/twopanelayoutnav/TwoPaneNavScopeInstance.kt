package com.microsoft.device.dualscreen.twopanelayout.twopanelayoutnav

import android.app.Activity
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

    override fun NavHostController.navigateUpTo(route: String?) {
        var finishActivity = false

        if (isSinglePane) {
            // Check that previous backstack entry route matches route parameter
            val prevRoute = previousBackStackEntry?.destination?.route
            if (prevRoute != route)
                throw IllegalArgumentException(
                    "Attempting to navigate up to $route, but previous backstack entry route is $prevRoute. " +
                        "Backstack: $backStack"
                )

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
                // Find target entry in backstack
                val targetEntry = backStack.findLast { entry -> entry.route == route }
                    ?: throw IllegalArgumentException(
                        "Attempting to navigate up to $route, but it's not in the backstack. Backstack: $backStack"
                    )
                val launchScreen = targetEntry.launchScreen

                // Check that target entry is the previous entry in the launch screen
                val currEntry = backStack.findLast { it.launchScreen == launchScreen }
                    ?: throw java.lang.IllegalStateException(
                        "Attempting to navigate up in $launchScreen, but no backstack entry found in that screen." +
                            "Backstack: $backStack"
                    )
                val prevEntry = backStack.findLast { it.launchScreen == launchScreen && it != currEntry }
                    ?: throw IllegalStateException(
                        "Attempting to navigate up in $launchScreen, but no previous backstack entry found" +
                            "in that screen. Backstack: $backStack"
                    )

                if (prevEntry.route != route)
                    throw IllegalArgumentException(
                        "Attempting to navigate up to $route in $launchScreen, but previous backstack entry " +
                            "route is ${prevEntry.route}. Backstack: $backStack"
                    )

                // Pop current destination from the launch screen
                currEntry.let { backStack.remove(currEntry) }

                // Navigate to the desired route in the launch screen
                when (launchScreen) {
                    Screen.Pane1 -> navigatePane1To(route)
                    Screen.Pane2 -> navigatePane2To(route)
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
