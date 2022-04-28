package com.microsoft.device.dualscreen.twopanelayout

import androidx.compose.runtime.Composable

data class Destination(val route: String, val content: @Composable TwoPaneScope.() -> Unit)
