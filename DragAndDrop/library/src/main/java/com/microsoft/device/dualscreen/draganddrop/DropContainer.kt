/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.draganddrop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned

@Composable
fun DropContainer(
    modifier: Modifier,
    onDrag: (inBounds: Boolean, isDragging: Boolean) -> Unit,
    content: @Composable (BoxScope.(data: DragData?) -> Unit)
) {
    val dragState = LocalDragState.current
    val dragPosition = dragState.dragPosition
    val dragOffset = dragState.dragOffset
    var inBounds by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.onGloballyPositioned {
            it.boundsInWindow().let { rect ->
                inBounds = rect.contains(dragPosition + dragOffset)
            }
        }
    ) {
        val dragData = if (inBounds) dragState.dragData else null
        onDrag(inBounds, dragState.isDragging)
        content(dragData)
    }
}
