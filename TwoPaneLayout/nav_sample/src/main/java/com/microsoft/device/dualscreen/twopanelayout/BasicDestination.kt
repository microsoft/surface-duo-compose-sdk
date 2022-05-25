package com.microsoft.device.dualscreen.twopanelayout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder

@Composable
fun TwoPaneNavScope.BasicDestination(
    navController: NavHostController,
    sampleDestination: SampleDestination,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Black,
) {
    // Calculate which destination should be navigated to next
    val nextDestination =
        SampleDestination.values().getOrElse(sampleDestination.ordinal + 1) { SampleDestination.DEST1 }

    // Set up the navigation options for a circular navigation pattern (1 -> 2 -> 3 -> 4 -> 1)
    val dest4NavOptions: NavOptionsBuilder.() -> Unit = {
        launchSingleTop = true
        restoreState = true
        popUpTo(SampleDestination.DEST1.route)
    }
    val emptyNavOptions: NavOptionsBuilder.() -> Unit = {}
    val navOptions: NavOptionsBuilder.() -> Unit = when (sampleDestination) {
        SampleDestination.DEST4 -> dest4NavOptions
        else -> emptyNavOptions
    }

    val onClick = { navController.navigateTo(nextDestination.route, sampleDestination.changesScreen, navOptions) }

    Scaffold(
        topBar = {
            // Customize top bar text depending on which pane a destination is shown in
            val pane = when {
                isSinglePane -> ""
                sampleDestination.route == currentPane1Destination -> " " + stringResource(R.string.pane1)
                sampleDestination.route == currentPane2Destination -> " " + stringResource(R.string.pane2)
                else -> ""
            }
            TopAppBar(pane)
        }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(sampleDestination.color)
                .clickable { onClick() }
                .padding(10.dp)
        ) {
            Text(
                modifier = Modifier.align(Alignment.TopCenter),
                text = stringResource(sampleDestination.text),
                color = textColor
            )
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                NavigationText(nextDestination.route, sampleDestination.changesScreen, textColor)
                NavigationGraphic(sampleDestination.next)
            }
        }
    }
}

@Composable
private fun NavigationText(nextRoute: String, inPane: Screen, textColor: Color) {
    Text(
        text = stringResource(R.string.navigates_to, nextRoute, inPane),
        color = textColor,
        style = MaterialTheme.typography.h4
    )
}

@Composable
private fun NavigationGraphic(nextId: Int) {
    Image(
        painter = painterResource(id = nextId),
        contentDescription = stringResource(id = R.string.image_description)
    )
}
