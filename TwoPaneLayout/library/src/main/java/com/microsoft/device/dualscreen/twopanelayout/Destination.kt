package com.microsoft.device.dualscreen.twopanelayout

import androidx.compose.runtime.Composable

/**
 * Class that represents TwoPaneLayoutNav destinations
 *
 * @param route: route of the destination to be used by a NavHost/NavHostController
 * @param content:
 */
data class Destination(val route: String, val content: @Composable TwoPaneNavScope.() -> Unit)
