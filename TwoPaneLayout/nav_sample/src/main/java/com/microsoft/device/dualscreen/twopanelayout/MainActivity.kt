package com.microsoft.device.dualscreen.twopanelayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.rememberNavController
import com.microsoft.device.dualscreen.twopanelayout.ui.theme.TwoPaneLayoutTheme
import com.microsoft.device.dualscreen.twopanelayout.ui.theme.blue
import com.microsoft.device.dualscreen.twopanelayout.ui.theme.green
import com.microsoft.device.dualscreen.twopanelayout.ui.theme.purple
import com.microsoft.device.dualscreen.twopanelayout.ui.theme.red
import com.microsoft.device.dualscreen.twopanelayout.ui.theme.yellow

enum class SampleDestination(
    number: Int,
    val color: Color,
    val text: Int,
    val changesScreen: Screen,
    val next: Int
) {
    DEST1(1, red, R.string.first_dest_text, Screen.Pane2, R.drawable.pane_2_purple),
    DEST2(2, purple, R.string.second_dest_text, Screen.Pane2, R.drawable.pane_2_green),
    DEST3(3, green, R.string.third_dest_text, Screen.Pane1, R.drawable.pane_1_yellow),
    DEST4(4, yellow, R.string.fourth_dest_text, Screen.Pane1, R.drawable.pane_1_red);

    val route = "destination $number"
}

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
                BasicDestination(
                    text = it.text,
                    color = it.color,
                    navController = navController,
                    sampleDestination = it,
                    inPane = it.changesScreen
                )
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

@Composable
private fun TwoPaneNavScope.BasicDestination(
    modifier: Modifier = Modifier,
    text: Int,
    color: Color,
    textColor: Color = Color.Black,
    navController: NavHostController,
    sampleDestination: SampleDestination,
    inPane: Screen
) {
    val nextDestination =
        SampleDestination.values().getOrElse(sampleDestination.ordinal + 1) { SampleDestination.DEST1 }
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

    val onClick = { navController.navigateTo(nextDestination.route, inPane, navOptions) }

    Scaffold(
        topBar = {
            val pane = when {
                isSinglePane -> ""
                sampleDestination.route == currentPane1Destination -> " - pane 1"
                sampleDestination.route == currentPane2Destination -> " - pane 2"
                else -> ""
            }
            TopAppBar(pane)
        }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(color)
                .clickable { onClick() }
                .padding(10.dp)
        ) {
            Text(modifier = Modifier.align(Alignment.TopCenter), text = stringResource(text), color = textColor)
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                NavigationText(nextDestination.route, inPane, textColor)
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
