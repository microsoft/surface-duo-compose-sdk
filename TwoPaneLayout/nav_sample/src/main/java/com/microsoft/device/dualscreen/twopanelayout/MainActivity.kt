package com.microsoft.device.dualscreen.twopanelayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.microsoft.device.dualscreen.twopanelayout.ui.theme.TwoPaneLayoutTheme
import com.microsoft.device.dualscreen.twopanelayout.ui.theme.blue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TwoPaneLayoutTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MainPage()
                }
            }
        }
    }
}

@Composable
fun MainPage() {
    val navController = rememberNavController()

    TwoPaneLayoutNav(
        navController = navController,
        destinations = SampleDestination.values().map {
            Destination(it.route) {
                BasicDestination(navController, it)
            }
        }.toTypedArray(),
        singlePaneStartDestination = SampleDestination.DEST1.route,
        pane1StartDestination = SampleDestination.DEST1.route,
        pane2StartDestination = SampleDestination.DEST2.route
    )
}

@Composable
fun TopAppBar(paneAnnotation: String) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name) + paneAnnotation, color = Color.White) },
        backgroundColor = blue
    )
}
