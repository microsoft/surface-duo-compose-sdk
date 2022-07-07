/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.device.dualscreen.draganddrop

/**
 * The mime types corresponding to all data types that can be dragged and dropped
 */
enum class MimeType(val value: String) {
    IMAGE_JPEG("image/jpeg"),
    TEXT_PLAIN("text/plain"),
    UNKNOWN_TYPE("unknown")
}
