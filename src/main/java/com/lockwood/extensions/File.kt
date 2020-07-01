package com.lockwood.extensions

import com.lockwood.constants.Desktop.STICKERS_DIRECTORY
import com.lockwood.constants.Image.PNG_IMAGE_FORMAT
import com.lockwood.model.Sticker
import java.io.File

fun String.makeDir() {
    val dir = File(this)
    dir.mkdir()
}

fun String.makeStickerPackDir() {
    STICKERS_DIRECTORY.makeDir()
    STICKERS_DIRECTORY.plus(File.separator).plus(this).makeDir()
}

fun Sticker.existInStickerPackDir(): Boolean {
    val fileName = id.toString().plus(File.separator).plus(PNG_IMAGE_FORMAT)
    val filePath = STICKERS_DIRECTORY.plus(File.separator).plus(fileName)

    return File(filePath).exists()
}