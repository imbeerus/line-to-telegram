package com.lockwood.constants

import com.lockwood.extensions.existInStickerPackDir
import com.lockwood.extensions.makeStickerPackDir
import com.lockwood.model.StickerPack
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

object LineWeb {
    const val STICKER_SHOP_URL = "https://store.line.me/stickershop"

    suspend fun saveStickersInPack(stickerPack: StickerPack) = with(stickerPack) {
        title.makeStickerPackDir()

        val stickersToSave = stickers.filterNot { it.existInStickerPackDir() }

        // coroutineScope allows you to launch coroutines inside and does not exit until all launched coroutines inside complete.
        // If one throws an exception, the sibling coroutines get cancelled and the exception is re-thrown from the scope.
        // coroutineScope is a suspending function, so while all the launched coroutines run in other threads on the threadpool, this suspending function is not resumed until they all complete
        coroutineScope {
            for (sticker in stickersToSave) {
                // Launches a separate coroutine for each sticker that needs to be downloaded
                launch {
                    val downloadedSticker = Image.readImageFromUrl(sticker.url)
                    Image.saveImage(downloadedSticker, sticker.name, sticker.packTitle)
                }
            }
        }
    }
}