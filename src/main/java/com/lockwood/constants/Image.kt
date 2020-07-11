package com.lockwood.constants

import com.lockwood.extensions.appendFileName
import com.lockwood.extensions.appendPath
import com.lockwood.extensions.getScaledInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.net.URL
import javax.imageio.ImageIO

object Image {

    const val PNG_IMAGE_FORMAT = "png"

    private const val TELEGRAM_STICKER_SIZE_PX = 512

    // WithContext suspends the current coroutine and runs its block in the specified coroutine context. Once the block is complete this coroutine resumes.
    // The IO dispatcher is designed for blocking I/O jobs. It shares a threadpool with the Default dispatcher but spawns new threads if none are available.
    suspend fun readImageFromUrl(
        url: String
    ): BufferedImage = withContext(Dispatchers.IO) {
        ImageIO.read(URL(url))
    }

    suspend fun saveImage(
        image: BufferedImage,
        name: String,
        directoryName: String
    ): Boolean {
        val absoluteImagePath = buildString {
            appendPath(Desktop.STICKERS_DIRECTORY)
            appendPath(directoryName)
            appendFileName(name, PNG_IMAGE_FORMAT)
        }

        return try {
            writeImageToFile(image, absoluteImagePath)
            true
        } catch (e: IOException) {
            false
        }
    }

    private suspend fun writeImageToFile(
        bufferedImage: BufferedImage,
        pathName: String
    ) = withContext(Dispatchers.IO) {
        val scaledImage = bufferedImage.getScaledInstance(TELEGRAM_STICKER_SIZE_PX)
        val outputFile = File(pathName)

        ImageIO.write(scaledImage, PNG_IMAGE_FORMAT, outputFile)
    }

}