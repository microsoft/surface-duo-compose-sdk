package com.microsoft.device.dualscreen.twopanelayout

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.microsoft.device.dualscreen.twopanelayout.ui.theme.TwoPaneLayoutTheme
import com.microsoft.device.dualscreen.twopanelayout.ui.theme.blue
import com.microsoft.device.dualscreen.twopanelayout.ui.theme.green
import com.microsoft.device.dualscreen.twopanelayout.ui.theme.yellow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TwoPaneLayoutTheme {
                MainPage()
            }
        }
    }
}

@Composable
fun MainPage() {
    val context = LocalContext.current

    TwoPaneLayout(
        paneMode = TwoPaneMode.HorizontalSingle,
        pane1 = { Pane1() },
        pane2 = { Pane2() },
        onPaneIncrease = { Toast.makeText(context, R.string.pane_increase, Toast.LENGTH_SHORT).show() },
        onPaneDecrease = { Toast.makeText(context, R.string.pane_decrease, Toast.LENGTH_SHORT).show() }
    )
}

@Composable
fun TwoPaneScope.TopAppBar(pane: Int) {
    val paneString = if (!isSinglePane) " " + stringResource(pane) else ""

    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name) + paneString,
                color = Color.White
            )
        },
        backgroundColor = blue
    )
}

@Composable
fun TwoPaneScope.Pane1() {
    Scaffold(
        topBar = { TopAppBar(R.string.pane1) }
    ) {
        Text(
            text = stringResource(R.string.first_pane_text),
            modifier = Modifier
                .background(color = green)
                .clickable { navigateToPane2() }
                .padding(10.dp)
                .fillMaxSize()
                .weight(.3f),
            color = Color.Black
        )
    }
}

@Composable
fun TwoPaneScope.Pane2() {
    Scaffold(
        topBar = { TopAppBar(R.string.pane2) }
    ) {
        Text(
            text = stringResource(R.string.second_pane_text),
            modifier = Modifier
                .background(color = yellow)
                .clickable { navigateToPane1() }
                .padding(10.dp)
                .fillMaxSize()
                .weight(.7f),
            color = Color.Black
        )
    }
}
