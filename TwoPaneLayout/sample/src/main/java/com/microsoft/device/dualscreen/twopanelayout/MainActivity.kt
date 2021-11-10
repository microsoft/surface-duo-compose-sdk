package com.microsoft.device.dualscreen.twopanelayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.microsoft.device.dualscreen.twopanelayout.ui.theme.TwoPaneLayoutTheme

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
    Scaffold(
        topBar = {
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
        },
        content = {
            TwoPaneLayout(
                paneMode = TwoPaneMode.HorizontalSingle,
                pane1 = {
                    Text(
                        text = stringResource(R.string.first_pane_text),
                        modifier = Modifier.fillMaxSize().background(color = Color.Cyan)
                            .clickable {
                                navigateToPane2()
                            }
                    )
                },
                pane2 = {
                    Text(
                        text = stringResource(R.string.second_pane_text),
                        modifier = Modifier.fillMaxSize().background(color = Color.Magenta)
                            .clickable {
                                navigateToPane1()
                            }
                    )
                }
            )
        }
    )
}
