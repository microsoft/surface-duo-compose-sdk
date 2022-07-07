package com.microsoft.device.dualscreen.draganddrop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.microsoft.device.dualscreen.draganddrop.ui.theme.DragAndDropTheme
import com.microsoft.device.dualscreen.draganddrop.ui.view.DragPane
import com.microsoft.device.dualscreen.draganddrop.ui.view.DropPane
import com.microsoft.device.dualscreen.twopanelayout.TwoPaneLayout
import com.microsoft.device.dualscreen.twopanelayout.TwoPaneScope

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DragAndDropTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    DragAndDropSample()
                }
            }
        }
    }
}

@Composable
fun DragAndDropSample() {
    DragContainer(modifier = Modifier.fillMaxSize()) {
        TwoPaneLayout(
            pane1 = { DragAndDropPane1() },
            pane2 = { DragAndDropPane2() }
        )
    }
}

@Composable
fun TwoPaneScope.DragAndDropPane1() {
    if (isSinglePane) {
        DragAndDropSinglePane()
    } else {
        DragPane()
    }
}

@Composable
fun DragAndDropPane2() {
    DropPane()
}

@Composable
fun DragAndDropSinglePane() {
    Column {
        DragPane(Modifier.weight(.4f))
        DropPane(Modifier.weight(.6f))
    }
}
