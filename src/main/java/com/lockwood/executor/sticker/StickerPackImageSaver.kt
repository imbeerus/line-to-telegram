package com.lockwood.executor.sticker

import com.lockwood.executor.Executor
import com.lockwood.extensions.existInStickerPackDir
import com.lockwood.extensions.makeStickerPackDir
import com.lockwood.model.StickerPack

class StickerPackImageSaver(
    private val stickerPack: StickerPack
) : Executor {

    override fun execute() = with(stickerPack) {
        title.makeStickerPackDir()

        val stickersToSave = stickers.filterNot { it.existInStickerPackDir() }

        stickersToSave.forEach {
            val imageExecutor = StickerImageExecutor(url = it.url)
            val stickerImage = imageExecutor.get()

            StickerImageSaver(sticker = it, image = stickerImage).execute()
        }
    }

}