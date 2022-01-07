/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.windowstate

import org.junit.Assert.assertFalse
import org.junit.Test

class WindowModeTest {
    @Test
    fun single_modes_are_not_dualscreen() {
        assertFalse(WindowMode.SINGLE_LANDSCAPE.isDualScreen)
        assertFalse(WindowMode.SINGLE_PORTRAIT.isDualScreen)
    }

    @Test
    fun dual_modes_are_dualscreen() {
        assert(WindowMode.DUAL_LANDSCAPE.isDualScreen)
        assert(WindowMode.DUAL_PORTRAIT.isDualScreen)
    }
}
