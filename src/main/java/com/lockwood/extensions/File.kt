package com.lockwood.extensions

import com.lockwood.model.Sticker
import java.io.File

val STICKERS_DIRECTORY = "${System.getProperty("user.dir")}${File.separator}Stickers"

fun String.makeDir() {
    val dir = File(this)
    dir.mkdir()
}

fun String.makeStickerPackDir() {
    STICKERS_DIRECTORY.makeDir()
    buildStickerPackPath(this).makeDir()
}

fun buildStickerPackPath(name: String) = buildString {
    appendPath(STICKERS_DIRECTORY)
    append(name)
}

fun Sticker.existInStickerPackDir(packTitle: String, fileExtension: String): Boolean {
    val fileName = buildString {
        appendFileName(id.toString(), fileExtension)
    }
    val filePath = buildString {
        appendPath(STICKERS_DIRECTORY)
        appendPath(packTitle)
        append(fileName)
    }

    return File(filePath).exists()
}