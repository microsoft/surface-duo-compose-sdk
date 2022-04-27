package com.microsoft.device.dualscreen.twopanelayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.microsoft.device.dualscreen.twopanelayout.ui.theme.TwoPaneLayoutTheme
import com.microsoft.device.dualscreen.twopanelayout.ui.theme.blue
import com.microsoft.device.dualscreen.twopanelayout.ui.theme.gray
import com.microsoft.device.dualscreen.twopanelayout.ui.theme.red
import com.microsoft.device.dualscreen.twopanelayout.ui.theme.yellow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TwoPaneLayoutTheme {
                // A surface container using the 'background' color from the theme
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

    val pane1 = Pane("pane1") { Pane1(navController) }
    val pane2 = Pane("pane2") { Pane2(navController) }
    val pane3 = Pane("pane3") { Pane3(navController) }
    val pane4 = Pane("pane4") { Pane4(navController) }

    Scaffold(
        topBar = { TopAppBar() },
        content = {
            TwoPaneLayout(
                navController = navController,
                paneMode = TwoPaneMode.HorizontalSingle,
                panes = arrayOf(pane1, pane2, pane3, pane4),
                singlePaneStartDestination = pane1.route,
                pane1StartDestination = pane1.route,
                pane2StartDestination = pane2.route
            )
        }
    )
}

@Composable
fun TopAppBar() {
    TopAppBar(
        title = {
            BasicText(
                text = stringResource(R.string.app_name),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
    )
}

@Composable
fun TwoPaneScope.Pane1(navController: NavHostController) {
    BasicPane(
        text = R.string.first_pane_text,
        color = blue,
        textColor = Color.White,
        navController = navController,
        to = "pane2",
        inPane = PaneContainer.Pane2
    )
}

@Composable
fun TwoPaneScope.Pane2(navController: NavHostController) {
    BasicPane(
        text = R.string.second_pane_text, color = gray, textColor = Color.White,
        navController = navController,
        to = "pane3",
        inPane = PaneContainer.Pane2
    )
}

@Composable
fun TwoPaneScope.Pane3(navController: NavHostController) {
    BasicPane(
        text = R.string.third_pane_text, color = yellow, textColor = Color.Black,
        navController = navController,
        to = "pane4",
        inPane = PaneContainer.Pane1
    )
}

@Composable
fun TwoPaneScope.Pane4(navController: NavHostController) {
    BasicPane(
        text = R.string.fourth_pane_text, color = red, textColor = Color.White,
        navController = navController,
        to = "pane1",
        inPane = PaneContainer.Pane1
    )
}

@Composable
private fun TwoPaneScope.BasicPane(
    modifier: Modifier = Modifier,
    text: Int,
    color: Color,
    textColor: Color,
    navController: NavHostController,
    to: String,
    inPane: PaneContainer
) {
    Box {
        Text(
            text = stringResource(text),
            modifier = modifier
                .background(color = color)
                .clickable { navController.navigateToPane(to, inPane) }
                .padding(10.dp)
                .fillMaxSize()
                .align(Alignment.TopCenter),
            color = textColor
        )
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            text = "Navigates to $to, inside $inPane when in two pane mode",
            color = textColor,
            style = MaterialTheme.typography.h4
        )
    }
}
