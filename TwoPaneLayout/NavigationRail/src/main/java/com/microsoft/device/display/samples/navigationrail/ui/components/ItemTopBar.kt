/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.navigationrail.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.microsoft.device.display.samples.navigationrail.R

private val ELEVATION = 0.dp
private val BUTTON_PADDING = 21.dp
private val BUTTON_SIZE = 40.dp
private val ButtonShape = RoundedCornerShape(3.dp)

/**
 * Custom top app bar design for showing a back button when looking at an item in detail
 *
 * @param onClick: action to perform when back button is clicked
 */
@Composable
fun ItemTopBar(onClick: () -> Unit) {
    TopAppBar(
        backgroundColor = Color.Transparent,
        elevation = ELEVATION,
        contentPadding = PaddingValues(
            start = BUTTON_PADDING,
            top = BUTTON_PADDING
        )
    ) {
        BackNavIcon(onClick)
    }
}

/**
 * Shows a back arrow on top of a rounded corner shape background
 *
 * @param onClick: action to perform when icon is clicked
 */
@Composable
fun BackNavIcon(onClick: () -> Unit) {
    IconButton(
        modifier = Modifier
            .size(BUTTON_SIZE)
            .background(MaterialTheme.colors.secondary, ButtonShape),
        onClick = { onClick() }
    ) {
        Icon(
            painter = painterResource(R.drawable.arrow_back_icon),
            contentDescription = stringResource(R.string.back),
            tint = MaterialTheme.colors.primary,
        )
    }
}
