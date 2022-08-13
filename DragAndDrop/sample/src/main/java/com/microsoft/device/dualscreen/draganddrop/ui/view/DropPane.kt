/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.draganddrop.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.microsoft.device.dualscreen.draganddrop.DropContainer
import com.microsoft.device.dualscreen.draganddrop.MimeType
import com.microsoft.device.dualscreen.draganddrop.R
import com.microsoft.device.dualscreen.draganddrop.ui.theme.Blue100
import com.microsoft.device.dualscreen.draganddrop.ui.theme.Blue200
import com.microsoft.device.dualscreen.draganddrop.ui.theme.Blue500
import com.microsoft.device.dualscreen.draganddrop.ui.theme.Shapes

@Composable
fun DropPane(modifier: Modifier = Modifier) {
    var dragText by remember { mutableStateOf<String?>(null) }
    var dragImage by remember { mutableStateOf<Painter?>(null) }
    val updateDragText: (String?) -> Unit = { newValue -> dragText = newValue }
    val updateDragImage: (Painter?) -> Unit = { newValue -> dragImage = newValue }
    var isDroppingItem by remember { mutableStateOf(false) }
    var isItemInBounds by remember { mutableStateOf(false) }

    DropContainer(
        modifier = modifier,
        onDrag = { inBounds, isDragging ->
            isDroppingItem = isDragging
            isItemInBounds = inBounds
        },
    ) { dragData ->
        val boxColor = if (isDroppingItem && isItemInBounds) Blue200 else if (isDroppingItem) Blue100 else Color.White
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp, start = 40.dp, end = 40.dp, bottom = 20.dp)
                .background(color = boxColor, shape = Shapes.large)
                .border(width = 2.dp, color = Blue500, shape = Shapes.large)
        ) {
            dragData?.let {
                if (!isDroppingItem) {
                    if (dragData.type == MimeType.TEXT_PLAIN) {
                        dragText = dragData.data as String
                        dragImage = null
                    }
                    if (dragData.type == MimeType.IMAGE_JPEG) {
                        dragImage = dragData.data as Painter
                        dragText = null
                    }
                }
            }

            DropPaneContent(dragText, dragImage)
            CloseButton(updateDragText, updateDragImage)
        }
    }
}

@Composable
fun DropPaneContent(dragText: String?, dragImage: Painter?) {
    if (dragText != null) {
        Text(
            text = dragText,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h3,
            color = Blue500
        )
    } else if (dragImage != null) {
        Image(
            painter = dragImage,
            contentDescription = stringResource(id = R.string.drop_image),
            modifier = Modifier.clip(Shapes.large)
        )
    } else {
        Text(
            text = stringResource(id = R.string.drop_placeholder),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h5
        )
    }
}

// to reset drag and drop
@Composable
fun CloseButton(
    updateDragText: (String?) -> Unit,
    updateDragImage: (Painter?) -> Unit
) {
    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = Modifier.fillMaxSize()
    ) {
        IconButton(
            onClick = {
                updateDragText(null)
                updateDragImage(null)
            }
        ) {
            Icon(
                tint = Color.Gray,
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(id = R.string.close_button)
            )
        }
    }
}
