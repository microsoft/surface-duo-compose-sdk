/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.navigationrail.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

private val InfoBoxShape = RoundedCornerShape(percent = 20)
private val INFO_BOX_HEIGHT = 60.dp
private val INFO_SPACE_BY = 10.dp
private val INFO_HORIZ_PADDING = 15.dp
private val INFO_BETWEEN_PADDING = 5.dp

/**
 * Shows a box with rounded corners that contains two kinds of information in a row, each with an
 * icon and a short text description
 *
 * @param icon1: drawable resource id for the first icon
 * @param info1: text for the first piece of information
 * @param description1: content description for first icon
 * @param icon2: drawable resource id for the second icon
 * @param info2: text for the second piece of information
 * @param description2: content description for second icon
 * @param textStyle: style with which the text information will be displayed
 */
@Composable
fun InfoBox(
    @DrawableRes icon1: Int,
    info1: String?,
    description1: String,
    @DrawableRes icon2: Int?,
    info2: String?,
    description2: String?,
    textStyle: TextStyle,
) {
    Row(
        modifier = Modifier
            .height(INFO_BOX_HEIGHT)
            .background(MaterialTheme.colors.secondaryVariant, InfoBoxShape)
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(INFO_SPACE_BY, Alignment.CenterHorizontally)
    ) {
        Spacer(Modifier.width(INFO_HORIZ_PADDING))
        info1?.let { info ->
            InfoIcon(icon1, description1)
            InfoText(info, textStyle)
            Spacer(Modifier.width(INFO_BETWEEN_PADDING))
        }
        info2?.let { info ->
            check(icon2 != null && description2 != null) { "Second fact exists in item but gallery does not have icon or content description for it " }
            InfoIcon(icon2, description2)
            InfoText(info, textStyle)
        }
        Spacer(Modifier.width(INFO_HORIZ_PADDING))
    }
}

@Composable
private fun InfoIcon(@DrawableRes icon: Int, description: String) {
    Icon(
        painter = painterResource(icon),
        contentDescription = description,
        tint = MaterialTheme.colors.onSurface
    )
}

@Composable
private fun InfoText(text: String, style: TextStyle) {
    Text(
        text = text,
        color = MaterialTheme.colors.onSurface,
        style = style,
        textAlign = TextAlign.Start
    )
}
