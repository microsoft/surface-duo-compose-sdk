/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.windowstate

/**
 * Class that represents the different modes in which content can be displayed in a window,
 * depending on window size and orientation
 *
 * Window modes:
 * - single portrait
 * - single landscape
 * - dual portrait
 * - dual landscape
 */
enum class WindowMode {
    SINGLE_PORTRAIT,
    SINGLE_LANDSCAPE,
    DUAL_PORTRAIT,
    DUAL_LANDSCAPE;

    val isDualScreen: Boolean get() = this == DUAL_PORTRAIT || this == DUAL_LANDSCAPE
}
