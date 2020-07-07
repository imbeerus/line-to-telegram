package com.lockwood.extensions

import java.awt.Dimension
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.nio.channels.Channels
import java.nio.channels.FileChannel
import java.nio.channels.ReadableByteChannel
import javax.imageio.ImageIO

private const val TELEGRAM_STICKER_SIZE_PX = 512

val File.extension: String
    get() = name.substringAfterLast(".")

fun scaleImagesForTelegram(
        path: String
) {
    val directory = File(path)
    directory.walk().forEach { file ->
        if (file.isFile) {
            val bufferedImage = ImageIO.read(file)
            val scaledImage = getResizedImage(bufferedImage)
            val outputFile = File(file.absolutePath)
            ImageIO.write(scaledImage, file.extension, outputFile)
        }
    }
}

fun saveImage(
        url: String,
        name: String,
        fileExtension: String,
        directoryName: String
) {
    val absoluteImagePath = buildString {
        appendPath(STICKERS_DIRECTORY)
        appendPath(directoryName)
        appendFileName(name, fileExtension)
    }

    try {
        writeImage(url, absoluteImagePath)
    } catch (e: IOException) {
        // do nothing
    }
}

private fun writeImage(
        url: String,
        path: String
) {
    val readableByteChannel: ReadableByteChannel = Channels.newChannel(URL(url).openStream())

    val fileOutputStream = FileOutputStream(path)
    val fileChannel: FileChannel = fileOutputStream.channel
    fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE)
}

private fun getResizedImage(
        img: BufferedImage
): BufferedImage {
    val dimension = getScaledDimension(img)
    val tmp = img.getScaledInstance(dimension.width, dimension.height, Image.SCALE_SMOOTH)
    val bufferedImage = BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB)
    bufferedImage.createGraphics().run {
        drawImage(tmp, 0, 0, null)
        dispose()
    }
    return bufferedImage
}

private fun getScaledDimension(
        img: BufferedImage
): Dimension {
    val width = img.width
    val height = img.height
    var newWidth = width
    var newHeight = height

    if (width != TELEGRAM_STICKER_SIZE_PX) {
        newWidth = TELEGRAM_STICKER_SIZE_PX
        newHeight = newWidth * height / width
    }
    if (newHeight > TELEGRAM_STICKER_SIZE_PX) {
        newHeight = TELEGRAM_STICKER_SIZE_PX
        newWidth = newHeight * width / height
    }
    return Dimension(newWidth, newHeight)
}