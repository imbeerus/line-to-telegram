package com.lockwood.constants

import com.lockwood.extensions.appendFileName
import com.lockwood.extensions.appendPath
import com.lockwood.extensions.getScaledInstance
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.net.URL
import javax.imageio.ImageIO

object Image {

    const val PNG_IMAGE_FORMAT = "png"

    private const val TELEGRAM_STICKER_SIZE_PX = 512

    fun readImageFromUrl(
        url: String
    ): BufferedImage = ImageIO.read(URL(url))

    fun saveImage(
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

    private fun writeImageToFile(
        bufferedImage: BufferedImage,
        pathName: String
    ) {
        val scaledImage = bufferedImage.getScaledInstance(TELEGRAM_STICKER_SIZE_PX)
        val outputFile = File(pathName)

        ImageIO.write(scaledImage, PNG_IMAGE_FORMAT, outputFile)
    }

}