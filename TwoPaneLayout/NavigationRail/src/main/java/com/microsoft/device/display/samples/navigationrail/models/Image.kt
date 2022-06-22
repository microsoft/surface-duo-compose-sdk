/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.display.samples.navigationrail.models

import androidx.annotation.DrawableRes

data class Image(
    val id: Int,
    @DrawableRes val image: Int,
    val name: String,
    val location: String,
    val fact1: String,
    val fact2: String,
    val details: String,
)
