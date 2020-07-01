package com.lockwood.executor.sticker

import com.lockwood.constants.Image.saveImage
import com.lockwood.executor.Executor
import com.lockwood.extensions.ioExecutor
import com.lockwood.model.Sticker
import java.awt.image.BufferedImage
import java.util.concurrent.ExecutorService

class StickerImageSaver(
    private val executor: ExecutorService = ioExecutor,
    private val sticker: Sticker,
    private val image: BufferedImage
) : Executor {

    override fun execute() = with(sticker) {
        executor.execute {
            saveImage(
                image = image,
                name = name,
                directoryName = packTitle
            )
        }
    }

}