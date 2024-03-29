/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.microsoft.device.dualscreen.draganddrop.DragData
import com.microsoft.device.dualscreen.draganddrop.DragTarget
import com.microsoft.device.dualscreen.draganddrop.MimeType
import com.microsoft.device.dualscreen.draganddrop.R
import com.microsoft.device.dualscreen.draganddrop.ui.theme.Blue500
import com.microsoft.device.dualscreen.draganddrop.ui.theme.Shapes

@Composable
fun DragPane(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DragTextBox()
            Spacer(modifier = Modifier.height(20.dp))
            DragImageBox(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun DragTextBox(modifier: Modifier = Modifier) {
    val dragText = stringResource(R.string.drag_text)
    val dragData = DragData(type = MimeType.TEXT_PLAIN, data = dragText)

    DragTarget(dragData = dragData) {
        Box(
            modifier = modifier.padding(top = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier.size(450.dp, 60.dp)
            ) {
                drawRoundRect(
                    color = Blue500,
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
                text = dragText,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5
            )
        }
    }
}

@Composable
fun DragImageBox(modifier: Modifier = Modifier) {
    val dragImage = painterResource(id = R.drawable.drag_and_drop_image)
    val dragData = DragData(type = MimeType.IMAGE_JPEG, data = dragImage)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(450.dp, 150.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(id = R.string.drag_action),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.weight(1f)
            )
            DragTarget(dragData = dragData) {
                Image(
                    painter = dragImage,
                    contentDescription = stringResource(id = R.string.drag_image),
                    modifier = Modifier
                        .weight(1f)
                        .clip(Shapes.large)
                )
            }
        }
    }
}
