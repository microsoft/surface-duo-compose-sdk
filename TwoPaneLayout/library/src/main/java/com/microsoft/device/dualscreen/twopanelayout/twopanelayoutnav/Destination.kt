package com.microsoft.device.dualscreen.twopanelayout.twopanelayoutnav

import androidx.compose.runtime.Composable
import com.microsoft.device.dualscreen.twopanelayout.TwoPaneNavScope

/**
 * Class that represents TwoPaneLayoutNav destinations
 *
 * @param route: route of the destination to be used by a NavHost/NavHostController
 * @param content:
 */
data class Destination(val route: String, val content: @Composable TwoPaneNavScope.() -> Unit)
