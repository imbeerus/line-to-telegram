package com.lockwood.executor.sticker

import com.lockwood.executor.Executor
import com.lockwood.extensions.buildStickerPackPath
import com.lockwood.extensions.ioExecutor
import com.lockwood.extensions.scaleImagesForTelegram
import java.util.concurrent.ExecutorService

class TelegramImageConverter(
        private val executor: ExecutorService = ioExecutor,
        private val folderName: String,
        private val isAnimated: Boolean
) : Executor {

    override fun execute() {
        executor.execute {
            val stickersDirectory = buildStickerPackPath(folderName)
            scaleImagesForTelegram(stickersDirectory)
        }
    }

}