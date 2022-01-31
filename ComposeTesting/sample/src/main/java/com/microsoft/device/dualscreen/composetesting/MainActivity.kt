/*
 *
 *  * Copyright (c) Microsoft Corporation. All rights reserved.
 *  * Licensed under the MIT License.
 *
 */

package com.microsoft.device.dualscreen.composetesting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.microsoft.device.dualscreen.composetesting.ui.theme.ComposeTestingTheme
import com.microsoft.device.dualscreen.twopanelayout.TwoPaneLayout

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTestingTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    ComposeTestingApp()
                }
            }
        }
    }
}

@Composable
fun ComposeTestingApp() {
    TwoPaneLayout(
        pane1 = {
            Text(text = stringResource(R.string.pane1_text))
        },
        pane2 = {
            Text(text = stringResource(R.string.pane2_text))
        }
    )
}
