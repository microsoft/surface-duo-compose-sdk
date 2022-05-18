package com.microsoft.device.dualscreen.twopanelayout

import androidx.compose.ui.graphics.Color
import com.microsoft.device.dualscreen.twopanelayout.ui.theme.green
import com.microsoft.device.dualscreen.twopanelayout.ui.theme.purple
import com.microsoft.device.dualscreen.twopanelayout.ui.theme.red
import com.microsoft.device.dualscreen.twopanelayout.ui.theme.yellow

enum class SampleDestination(
    val number: Int,
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
