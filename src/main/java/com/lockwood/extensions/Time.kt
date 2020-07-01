package com.lockwood.extensions

import java.text.SimpleDateFormat
import java.util.*

private const val PRINT_TIME_FORMAT = "HH:mm:ss"

val currentTime: String
    get() {
        val dateFormat = SimpleDateFormat(PRINT_TIME_FORMAT)
        val now = Date()
        return dateFormat.format(now)
    }