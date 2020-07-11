package com.lockwood.extensions

import com.lockwood.model.StickerPack
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

suspend fun saveStickers(stickerPack: StickerPack) = with(stickerPack) {
    title.makeStickerPackDir()

    val stickersToSave = stickers.filterNot { sticker ->
        sticker.existInStickerPackDir(stickerPack.title, sticker.imageFormat)
    }

    // coroutineScope allows you to launch coroutines inside and does not exit until all launched coroutines inside complete.
    // If one throws an exception, the sibling coroutines get cancelled and the exception is re-thrown from the scope.
    // coroutineScope is a suspending function, so while all the launched coroutines run in other threads on the threadpool, this suspending function is not resumed until they all complete
    coroutineScope {
        stickersToSave.forEach {
            launch {
                saveImage(
                    url = it.url,
                    name = it.name,
                    fileExtension = it.imageFormat,
                    directoryName = it.packTitle
                )
            }
        }
    }
}
