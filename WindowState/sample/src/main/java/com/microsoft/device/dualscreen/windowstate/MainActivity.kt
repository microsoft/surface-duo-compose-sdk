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
    val foldSizeDp = windowState.foldSizeDp
    val pane1Size = windowState.pane1SizeDp()
    val pane2Size = windowState.pane2SizeDp()

    var foldSizePx: Float
    var pane1WidthPx: Float
    var pane1HeightPx: Float
    var pane2WidthPx: Float
    var pane2HeightPx: Float
    with(LocalDensity.current) {
        foldSizePx = foldSizeDp.toPx()
        pane1WidthPx = pane1Size.width.dp.toPx()
        pane1HeightPx = pane1Size.height.dp.toPx()
        pane2WidthPx = pane2Size.width.dp.toPx()
        pane2HeightPx = pane2Size.height.dp.toPx()
    }

    val isDualScreen = windowState.isDualScreen()
    val windowMode = windowState.windowMode

    val widthSizeClass = windowState.widthSizeClass()
    val heightSizeClass = windowState.heightSizeClass()

    Column {
        Text(text = "Foldable properties", style = MaterialTheme.typography.h6)
        Text(text = "The current foldSizeDp is ${foldSizePx}px, $foldSizeDp")
        Text(text = "The current pane1SizeDp is:\n\t[${pane1WidthPx}px, ${pane1HeightPx}px], [${pane1Size.width.dp}, ${pane1Size.height.dp}]")
        Text(text = "The current pane2SizeDp is:\n\t[${pane2WidthPx}px, ${pane2HeightPx}px], [${pane2Size.width.dp}, ${pane2Size.height.dp}]")
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
