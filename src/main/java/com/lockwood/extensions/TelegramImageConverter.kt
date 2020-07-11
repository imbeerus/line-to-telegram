package com.lockwood.extensions

import com.lockwood.extensions.buildStickerPackPath
import com.lockwood.extensions.scaleImagesForTelegram

suspend fun convertImagesInFolderForTelegram(folderName: String) {
    val stickersDirectory = buildStickerPackPath(folderName)
    scaleImagesForTelegram(stickersDirectory)
}
