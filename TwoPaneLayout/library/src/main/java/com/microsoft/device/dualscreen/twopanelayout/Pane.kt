package com.microsoft.device.dualscreen.twopanelayout

import androidx.compose.runtime.Composable

data class Pane(val route: String, val content: @Composable TwoPaneScope.() -> Unit)
