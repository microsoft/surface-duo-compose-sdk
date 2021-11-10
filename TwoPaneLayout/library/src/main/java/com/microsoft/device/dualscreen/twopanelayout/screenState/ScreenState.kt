/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.twopanelayout.screenState

import android.graphics.Rect
import androidx.compose.ui.geometry.Size

/**
 * LayoutOrientation
 *     Horizontal,  the width of hinge/folding line is bigger than the height, top/bottom
 *     Vertical     the height of hinge/folding line is bigger than the width, left/right
 */
enum class LayoutOrientation {
    Horizontal,
    Vertical
}

/**
 * LayoutState
 * Open,        two-pane layout display, it is always "Open" for big-screen device
 * Fold         single layout display, including single-screen phone, foldable device in folding mode and app in un-spanned mode
 */
enum class LayoutState {
    Open,
    Fold
}

/**
 * DeviceType
 *     Single,    // regular single-screen device, such as single-screen phone
 *     Dual,  // dual-screen/foldable device, such as Surface Duo device, Samsung Galaxy Fold 2
 *     Big        // large-screen device, such as tablet
 */
enum class DeviceType {
    Single,
    Dual,
    Big
}

class ScreenState(
    val deviceType: DeviceType,
    val screenSize: Size,
    var hingeBounds: Rect,
    var orientation: LayoutOrientation,
    var layoutState: LayoutState
) {
    val paneSize: Size
        get() {
            if (deviceType == DeviceType.Big) {
                return if (orientation == LayoutOrientation.Vertical) {
                    Size(width = screenSize.width / 2, height = screenSize.height)
                } else {
                    Size(width = screenSize.width, height = screenSize.height / 2)
                }
            } else if (deviceType == DeviceType.Dual) {
                return if (orientation == LayoutOrientation.Vertical) {
                    Size(
                        width = hingeBounds.left.toFloat(),
                        height = hingeBounds.height().toFloat()
                    )
                } else {
                    Size(
                        width = hingeBounds.width().toFloat(),
                        height = hingeBounds.top.toFloat()
                    )
                }
            }
            return screenSize
        }
}
