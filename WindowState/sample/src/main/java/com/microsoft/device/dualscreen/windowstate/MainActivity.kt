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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
    windowState.largeScreenPane1Weight = 0.3f
    val pane1Size = windowState.pane1SizeDp
    val pane2Size = windowState.pane2SizeDp

    var foldSizePx: Float
    var pane1WidthPx: Float
    var pane1HeightPx: Float
    var pane2WidthPx: Float
    var pane2HeightPx: Float
    with(LocalDensity.current) {
        foldSizePx = foldSizeDp.toPx()
        pane1WidthPx = pane1Size.width.toPx()
        pane1HeightPx = pane1Size.height.toPx()
        pane2WidthPx = pane2Size.width.toPx()
        pane2HeightPx = pane2Size.height.toPx()
    }

    val isDualScreen = windowState.isDualScreen()
    val windowMode = windowState.windowMode

    val widthSizeClass = windowState.widthSizeClass()
    val heightSizeClass = windowState.heightSizeClass()

    Column(
        modifier = Modifier
            .padding(30.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(text = "WindowState sample", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(15.dp))

        Text(text = "Large screen and foldable properties", style = MaterialTheme.typography.h6)
        Text(text = "The current foldSizeDp is ${foldSizePx}px, $foldSizeDp")
        Text(text = "The current largeScreenPane1Weight is ${windowState.largeScreenPane1Weight}")
        Text(text = "The current pane1SizeDp is:\n\t[${pane1WidthPx}px, ${pane1HeightPx}px], [${pane1Size.width}, ${pane1Size.height}]")
        Text(text = "The current pane2SizeDp is:\n\t[${pane2WidthPx}px, ${pane2HeightPx}px], [${pane2Size.width}, ${pane2Size.height}]")
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
