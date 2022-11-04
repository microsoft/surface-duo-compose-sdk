package com.microsoft.device.dualscreen.twopanelayout.twopanelayoutnav

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.get
import com.microsoft.device.dualscreen.twopanelayout.Screen
import com.microsoft.device.dualscreen.twopanelayout.TwoPaneNavScope

/**
 * Class that represents an entry in the manual TwoPaneLayoutNav backstack
 *
 * @param route: route of the destination in the entry
 * @param launchScreen: screen in which the entry was shown in two pane mode
 */
internal data class TwoPaneBackStackEntry(val route: String, val launchScreen: Screen)

internal fun MutableList<TwoPaneBackStackEntry>.initialize(
    startDestination1: String,
    startDestination2: String? = null
) {
    check(this.isEmpty()) { "Attempting to initialize non-empty back stack" }

    // REVISIT: assumes that single pane start destination should be assigned to pane 1
    add(TwoPaneBackStackEntry(startDestination1, Screen.Pane1))
    startDestination2?.let { add(TwoPaneBackStackEntry(startDestination2, Screen.Pane2)) }
}

/**
 * Add the [Composable] to the [NavGraphBuilder]
 *
 * @param route route for the destination
 * @param arguments list of arguments to associate with destination
 * @param deepLinks list of deep links to associate with the destinations
 * @param content composable for the destination
 */
fun NavGraphBuilder.composable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable TwoPaneNavScope.(NavBackStackEntry) -> Unit
) {
    graphContent[route] = content

    addDestination(
        ComposeNavigator.Destination(provider[ComposeNavigator::class]) { TwoPaneNavScopeInstance.content(it) }
            .apply {
                this.route = route
                arguments.forEach { (argumentName, argument) ->
                    addArgument(argumentName, argument)
                }
                deepLinks.forEach { deepLink ->
                    addDeepLink(deepLink)
                }
            }
    )
}
