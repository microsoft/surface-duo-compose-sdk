package com.microsoft.device.dualscreen.twopanelayout.common

import android.os.Build
import android.view.WindowInsets
import android.view.WindowManager
import android.view.WindowMetrics
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import com.microsoft.device.dualscreen.windowstate.WindowState

@Composable
fun calculatePaneSizes(windowState: WindowState): Array<Size> {
    var pane1SizePx: Size
    var pane2SizePx: Size
    with(LocalDensity.current) {
        pane1SizePx = windowState.pane1SizeDp.toSize()
        pane2SizePx = windowState.pane2SizeDp.toSize()
    }

    // Calculate insets padding if api R or above
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowContext = LocalContext.current.createWindowContext(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, null)
        val windowManager = windowContext.getSystemService(WindowManager::class.java)
        val metrics: WindowMetrics = windowManager.currentWindowMetrics

        val windowInsets: WindowInsets = metrics.windowInsets
        val statusInsets = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.statusBars())
        val navigationInsets = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars())

        // Adjust WindowState pane size according to posture/orientation
        when {
            windowState.isDualPortrait() -> {
                if (navigationInsets.left > 0) {
                    pane1SizePx = Size(pane1SizePx.width - navigationInsets.left, pane1SizePx.height)
                    pane2SizePx = Size(pane2SizePx.width, pane2SizePx.height)
                } else {
                    pane1SizePx = Size(pane1SizePx.width, pane1SizePx.height)
                    pane2SizePx = Size(pane2SizePx.width - navigationInsets.right, pane2SizePx.height)
                }
            }
            windowState.isDualLandscape() -> {
                pane1SizePx = Size(pane1SizePx.width, pane1SizePx.height - statusInsets.top)
                pane2SizePx = Size(pane2SizePx.width, pane2SizePx.height - navigationInsets.bottom)
            }
        }
    }

    return arrayOf(pane1SizePx, pane2SizePx)
}
