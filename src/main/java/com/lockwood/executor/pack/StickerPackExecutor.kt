package com.lockwood.executor.pack

import com.lockwood.executor.DataExecutor
import com.lockwood.extensions.networkExecutor
import com.lockwood.extensions.parseId
import com.lockwood.extensions.switchThreadName
import com.lockwood.model.StickerPack
import com.lockwood.parser.StickerPackParser
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

@ExperimentalStdlibApi
class StickerPackExecutor(
        private val executor: ExecutorService = networkExecutor,
        private val parser: StickerPackParser,
        private val link: String
) : DataExecutor<StickerPack> {

    override fun submit(): Future<StickerPack> {
        return executor.submit(Callable {
            val productId = link.parseId()
            val threadNumber = "sticker-pack-download-$productId"
            switchThreadName(threadNumber)

            parser.parseStickerPack(link)
        })
    }

    override fun get(): StickerPack {
        return submit().get()
    }

}