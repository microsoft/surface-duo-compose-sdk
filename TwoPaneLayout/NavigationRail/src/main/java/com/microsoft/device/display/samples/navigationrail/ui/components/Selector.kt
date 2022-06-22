/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.navigationrail.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val DEFAULT_SIZE = 50.dp
private val SelectorShape = RoundedCornerShape(percent = 15)

/**
 * Reference:https://github.com/android/compose-samples/blob/main/Jetsnack/app/src/main/java/com/example/jetsnack/ui/home/Home.kt
 *
 * Creates a graphic that indicates that an item is currently selected
 *
 * @param size: optional parameter that sets the size of the selector in dp (default is 50.dp)
 * @param color: optional parameter that sets the color of the selector (default is secondary color
 * from theme)
 */
@Composable
fun Selector(size: Dp = DEFAULT_SIZE, color: Color = MaterialTheme.colors.secondary) {
    Spacer(
        modifier = Modifier
            .size(size)
            .background(color, SelectorShape)
    )
}
