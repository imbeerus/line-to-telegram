package com.lockwood.executor.sticker

import com.lockwood.executor.Executor
import com.lockwood.extensions.networkExecutor
import com.lockwood.extensions.saveImage
import com.lockwood.model.Sticker
import java.util.concurrent.ExecutorService

class StickerImageSaver(
        private val executor: ExecutorService = networkExecutor,
        private val sticker: Sticker
) : Executor {

    override fun execute() = with(sticker) {
        executor.execute {
            saveImage(
                    url = sticker.url,
                    name = name,
                    fileExtension = sticker.imageFormat,
                    directoryName = packTitle
            )
        }
    }

}