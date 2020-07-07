package com.lockwood.extensions

import java.text.SimpleDateFormat
import java.util.*

val currentTime: String
    get() {
        val dateFormat = SimpleDateFormat("HH:mm:ss")
        val now = Date()
        return dateFormat.format(now)
    }