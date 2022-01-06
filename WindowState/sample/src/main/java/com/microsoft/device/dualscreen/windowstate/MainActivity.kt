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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
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
    val foldSize = windowState.foldSize.dp
    val foldablePaneWidth = windowState.foldablePaneWidth
    val isDualScreen = windowState.isDualScreen()
    val isDualPortrait = windowState.isDualPortrait()
    val isDualLandscape = windowState.isDualLandscape()
    val isSinglePortrait = windowState.isSinglePortrait()
    val isSingleLandscape = windowState.isSingleLandscape()

    val windowWidth = LocalConfiguration.current.screenWidthDp.dp
    val windowHeight = LocalConfiguration.current.screenHeightDp.dp
    val widthSizeClass = getWindowSizeClass(windowWidth)
    val heightSizeClass = getWindowSizeClass(windowHeight, Dimension.HEIGHT)

    Column {
        Text(text = "The current foldSize is $foldSize")
        Text(text = "The current foldablePaneWidth is $foldablePaneWidth")

        Text(text = "Is the app in dual screen mode? $isDualScreen")
        Text(text = "Is the app in dual portrait mode? $isDualPortrait")
        Text(text = "Is the app in dual landscape mode? $isDualLandscape")
        Text(text = "Is the app in single portrait mode? $isSinglePortrait")
        Text(text = "Is the app in single screen mode? $isSingleLandscape")

        Text(text = "What is the current width size class? $widthSizeClass")
        Text(text = "What is the current height size class? $heightSizeClass")
    }
}
