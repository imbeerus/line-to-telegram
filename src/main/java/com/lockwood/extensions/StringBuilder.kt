package com.lockwood.extensions

import java.io.File

fun StringBuilder.appendPath(
        string: String
): StringBuilder {
    return run {
        append(string)
        append(File.separator)
    }
}

fun StringBuilder.appendFileName(
        name: String,
        extension: String
): StringBuilder {
    return run {
        append(name)
        append(".")
        append(extension)
    }
}