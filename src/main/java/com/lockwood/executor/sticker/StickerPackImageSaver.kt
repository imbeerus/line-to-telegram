package com.lockwood.executor.sticker

import com.lockwood.executor.Executor
import com.lockwood.extensions.buildStickerPackPath
import com.lockwood.extensions.existInStickerPackDir
import com.lockwood.extensions.makeStickerPackDir
import com.lockwood.extensions.scaleImagesForTelegram
import com.lockwood.model.StickerPack

class StickerPackImageSaver(
    private val stickerPack: StickerPack
) : Executor {

    override fun execute() = with(stickerPack) {
        title.makeStickerPackDir()

        val stickersToSave = stickers.filterNot { sticker ->
            sticker.existInStickerPackDir(stickerPack.title, sticker.imageFormat)
        }

        stickersToSave.forEach { StickerImageSaver(sticker = it).execute() }
        scaleImagesForTelegram(buildStickerPackPath(stickerPack.title))
    }

}