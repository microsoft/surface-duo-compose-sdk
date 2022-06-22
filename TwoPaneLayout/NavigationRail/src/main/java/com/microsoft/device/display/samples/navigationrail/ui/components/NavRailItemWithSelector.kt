/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.navigationrail.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@ExperimentalAnimationApi
@Composable
fun ColumnScope.NavRailItemWithSelector(
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    selected: Boolean,
    onClick: () -> Unit,
    selectedContentColor: Color,
    unselectedContentColor: Color,
) {
    Box(modifier = Modifier.weight(1f, fill = false), contentAlignment = Alignment.Center) {
        Column {
            AnimatedVisibility(
                visible = selected,
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                Selector()
            }
        }
        Column {
            NavigationRailItem(
                icon = icon,
                label = label,
                selected = selected,
                onClick = onClick,
                selectedContentColor = selectedContentColor,
                unselectedContentColor = unselectedContentColor
            )
        }
    }
}
