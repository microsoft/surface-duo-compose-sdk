package com.microsoft.device.dualscreen.twopanelayout

import androidx.activity.ComponentActivity
import java.util.concurrent.CountDownLatch

class TestActivity : ComponentActivity() {
    var hasFocusLatch = CountDownLatch(1)

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hasFocusLatch.countDown()
        }
    }
}
