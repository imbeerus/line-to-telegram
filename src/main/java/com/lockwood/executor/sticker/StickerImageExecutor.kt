package com.lockwood.executor.sticker

import com.lockwood.constants.Image.readImageFromUrl
import com.lockwood.executor.DataExecutor
import com.lockwood.extensions.networkExecutor
import com.lockwood.extensions.parseProductId
import com.lockwood.extensions.switchThreadName
import java.awt.image.BufferedImage
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

class StickerImageExecutor(
    private val executor: ExecutorService = networkExecutor,
    private val url: String
) : DataExecutor<BufferedImage> {

    override fun submit(): Future<BufferedImage> {
        return executor.submit(Callable {
            val threadNumber = "image-read-${url.parseProductId()}"
            switchThreadName(threadNumber)

            readImageFromUrl(url)
        })
    }

    override fun get(): BufferedImage {
        return submit().get()
    }

}