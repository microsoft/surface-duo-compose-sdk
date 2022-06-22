/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.navigationrail.ui.view

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.microsoft.device.display.samples.navigationrail.R
import com.microsoft.device.display.samples.navigationrail.models.Image
import com.microsoft.device.display.samples.navigationrail.ui.components.ContentDrawer
import com.microsoft.device.display.samples.navigationrail.ui.components.InfoBox

private lateinit var BodyTextStyle: TextStyle
private lateinit var SubtitleTextStyle: TextStyle
private const val EXPANDED_HEIGHT_2PANE = 0.7f
private const val EXPANDED_HEIGHT_1PANE_PORTRAIT = 0.55f
private const val EXPANDED_HEIGHT_1PANE_LANDSCAPE = 0.65f
private const val COLLAPSED_HEIGHT_2PANE = 0.4f
private const val COLLAPSED_HEIGHT_1PANE_PORTRAIT = 0.28f
private const val COLLAPSED_HEIGHT_1PANE_LANDSCAPE = 0.357f
private val PILL_TOP_PADDING = 8.dp
private val NAME_TOP_PADDING = 8.dp
private val LOCATION_TOP_PADDING = 10.dp
private val LOCATION_BETWEEN_PADDING = 5.dp
private val CONDITIONS_TOP_PADDING = 15.dp
private val LONG_DETAILS_TOP_PADDING = 35.dp
private val LONG_DETAILS_BOTTOM_PADDING = 10.dp
private const val LONG_DETAILS_LINE_HEIGHT = 32f

@ExperimentalUnitApi
@ExperimentalMaterialApi
@Composable
fun BoxWithConstraintsScope.ItemDetailsDrawer(
    image: Image,
    isDualLandscape: Boolean,
    isDualPortrait: Boolean,
    foldIsOccluding: Boolean,
    foldBoundsDp: DpRect,
    windowHeight: Dp,
    gallerySection: GallerySections?,
) {
    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

    // Set max/min height for drawer based on orientation
    val expandedHeightPct: Float
    val collapsedHeightPct: Float
    when {
        isDualLandscape -> {
            expandedHeightPct = EXPANDED_HEIGHT_2PANE
            collapsedHeightPct = COLLAPSED_HEIGHT_2PANE
        }
        isDualPortrait || isPortrait -> {
            expandedHeightPct = EXPANDED_HEIGHT_1PANE_PORTRAIT
            collapsedHeightPct = COLLAPSED_HEIGHT_1PANE_PORTRAIT
        }
        else -> {
            expandedHeightPct = EXPANDED_HEIGHT_1PANE_LANDSCAPE
            collapsedHeightPct = COLLAPSED_HEIGHT_1PANE_LANDSCAPE
        }
    }

    // Set text size for drawer based on orientation
    if (isDualLandscape) {
        BodyTextStyle = MaterialTheme.typography.body2
        SubtitleTextStyle = MaterialTheme.typography.subtitle2
    } else {
        BodyTextStyle = MaterialTheme.typography.body1
        SubtitleTextStyle = MaterialTheme.typography.subtitle1
    }

    ContentDrawer(
        expandedHeightPct = expandedHeightPct,
        collapsedHeightPct = collapsedHeightPct,
        foldIsOccluding = foldIsOccluding && isDualLandscape,
        foldBoundsDp = foldBoundsDp,
        foldBottomPaddingDp = LONG_DETAILS_TOP_PADDING,
        windowHeightDp = windowHeight,
        hiddenContent = { ItemDetailsLong(image.details) }
    ) {
        DrawerPill()
        ItemName(image.name)
        ItemLocation(image.location)
        ItemConditions(gallerySection, image.fact1, image.fact2)
    }
}

@Composable
private fun ColumnScope.DrawerPill() {
    Spacer(Modifier.height(PILL_TOP_PADDING))
    Icon(
        painter = painterResource(R.drawable.drawer_pill),
        contentDescription = stringResource(R.string.drawer_pill),
        tint = if (MaterialTheme.colors.isLight)
            MaterialTheme.colors.primary
        else
            MaterialTheme.colors.secondaryVariant,
        modifier = Modifier.align(Alignment.CenterHorizontally)
    )
}

@Composable
private fun ItemName(name: String) {
    Spacer(Modifier.height(NAME_TOP_PADDING))
    Text(
        text = name,
        color = MaterialTheme.colors.onSurface,
        style = BodyTextStyle,
        textAlign = TextAlign.Start
    )
}

@Composable
private fun ItemLocation(location: String) {
    Spacer(Modifier.height(LOCATION_TOP_PADDING))
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.location_icon),
            contentDescription = stringResource(R.string.location),
            tint = MaterialTheme.colors.onSurface
        )
        Spacer(Modifier.width(LOCATION_BETWEEN_PADDING))
        Text(
            text = location,
            color = MaterialTheme.colors.onSurface,
            style = SubtitleTextStyle,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
private fun ItemConditions(gallerySection: GallerySections?, fact1: String, fact2: String) {
    Spacer(Modifier.height(CONDITIONS_TOP_PADDING))
    gallerySection?.let { section ->
        InfoBox(
            icon1 = section.fact1Icon,
            info1 = if (fact1 == "") null else fact1,
            description1 = stringResource(section.fact1Description),
            icon2 = section.fact2Icon,
            info2 = if (fact2 == "") null else fact2,
            description2 = section.fact2Description?.let { stringResource(it) },
            textStyle = SubtitleTextStyle
        )
    }
}

@ExperimentalUnitApi
@Composable
private fun ItemDetailsLong(details: String) {
    val scrollState = rememberScrollState()

    Text(
        modifier = Modifier
            .padding(bottom = LONG_DETAILS_BOTTOM_PADDING)
            .verticalScroll(scrollState),
        text = details,
        color = MaterialTheme.colors.onSurface,
        style = BodyTextStyle,
        textAlign = TextAlign.Start,
        overflow = TextOverflow.Visible,
        lineHeight = TextUnit(LONG_DETAILS_LINE_HEIGHT, TextUnitType.Sp)
    )
}
