package com.lockwood.executor.pack

import com.lockwood.executor.DataExecutor
import com.lockwood.extensions.networkExecutor
import com.lockwood.extensions.parseProductId
import com.lockwood.extensions.switchThreadName
import com.lockwood.model.StickerPack
import parseStickerPack
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

@ExperimentalStdlibApi
class StickerPackExecutor(
    private val executor: ExecutorService = networkExecutor,
    private val stickerPageLink: String
) : DataExecutor<StickerPack> {

    override fun submit(): Future<StickerPack> {
        return executor.submit(Callable {
            val productId = stickerPageLink.parseProductId()
            val threadNumber = "sticker-pack-download-$productId"
            switchThreadName(threadNumber)

            parseStickerPack(stickerPageLink)
        })
    }

    override fun get(): StickerPack {
        return submit().get()
    }

}