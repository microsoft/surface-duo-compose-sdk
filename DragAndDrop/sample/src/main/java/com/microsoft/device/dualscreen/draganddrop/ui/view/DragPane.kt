/*
 *
 *  * Copyright (c) Microsoft Corporation. All rights reserved.
 *  * Licensed under the MIT License.
 *
 */

package com.microsoft.device.dualscreen.draganddrop.ui.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.microsoft.device.dualscreen.draganddrop.R
import com.microsoft.device.dualscreen.draganddrop.ui.theme.Purple200

@Composable
fun DragPane(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween
        ) {
            DragTextBox(Modifier.weight(.3f))
            Spacer(modifier = Modifier.height(20.dp))
            DragImageBox(Modifier.weight(.7f))
        }
    }
}

@Composable
fun DragTextBox(modifier: Modifier) {
    Box(
        modifier = modifier.padding(top = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.size(450.dp, 60.dp)
        ) {
            drawRoundRect(
                color = Purple200,
                cornerRadius = CornerRadius(20f),
                style = Stroke(
                    width = 5f,
                    pathEffect = PathEffect.dashPathEffect(
                        intervals = floatArrayOf(20f, 10f),
                        phase = 10f
                    )
                )
            )
        }
        Text(
            text = "Drag me",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h5
        )
    }
}

@Composable
fun DragImageBox(modifier: Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(500.dp, 150.dp)
    ){
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "or drag:",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.weight(1f)
            )
            Image(
                painter = painterResource(id = R.drawable.drag_and_drop_image),
                contentDescription = null,
                modifier = Modifier.weight(1f)
            )
        }
    }
}