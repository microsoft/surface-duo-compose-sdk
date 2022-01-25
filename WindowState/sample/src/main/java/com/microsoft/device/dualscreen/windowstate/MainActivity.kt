/*
 *
 *  * Copyright (c) Microsoft Corporation. All rights reserved.
 *  * Licensed under the MIT License.
 *
 */

package com.microsoft.device.dualscreen.windowstate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.microsoft.device.dualscreen.windowstate.ui.theme.WindowStateTheme

class MainActivity : ComponentActivity() {
    private lateinit var windowState: WindowState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            windowState = rememberWindowState()

            WindowStateTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    WindowStateDashboard(windowState)
                }
            }
        }
    }
}

@Composable
fun WindowStateDashboard(windowState: WindowState) {
    val foldablePaneWidth = windowState.foldablePaneWidth
    val foldablePaneWidthDp = with(LocalDensity.current) { foldablePaneWidth.toDp() }
    val foldSize = windowState.foldSizeDp
    var foldSizePx: Float
    with(LocalDensity.current) {
        foldSizePx = foldSize.toPx()
    }

    val isDualScreen = windowState.isDualScreen()
    val windowMode = windowState.windowMode

    val widthSizeClass = windowState.widthSizeClass()
    val heightSizeClass = windowState.heightSizeClass()

    Column {
        Text(text = "The current foldablePaneWidth is ${foldablePaneWidth}px, $foldablePaneWidthDp")
        Text(text = "Foldable properties", style = MaterialTheme.typography.h6)
        Text(text = "The current foldSize is ${foldSizePx}px, $foldSize")
        Spacer(modifier = Modifier.height(15.dp))

        Text(text = "Window mode properties", style = MaterialTheme.typography.h6)
        Text(text = "Is the app in dual screen mode? $isDualScreen")
        Text(text = "The current window mode is $windowMode")
        Spacer(modifier = Modifier.height(15.dp))

        Text(text = "Window size properties", style = MaterialTheme.typography.h6)
        Text(text = "What is the current width size class? $widthSizeClass")
        Text(text = "What is the current height size class? $heightSizeClass")
    }
}
