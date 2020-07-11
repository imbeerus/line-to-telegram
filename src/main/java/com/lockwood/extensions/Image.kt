package com.lockwood.extensions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

suspend fun scaleImagesForTelegram(
        path: String
) {
    val directory = File(path)

    // coroutineScope allows you to launch coroutines inside and does not exit until all launched coroutines inside complete.
    // If one throws an exception, the sibling coroutines get cancelled and the exception is re-thrown from the scope.
    // coroutineScope is a suspending function, so while all the launched coroutines run in other threads on the threadpool, this suspending function is not resumed until they all complete
    coroutineScope {
        directory.walk().forEach { file ->
            if (file.isFile) {

                // You can specify that you want to launch the coroutine on another dispatcher if you want. In this case we choose the IO dispatcher because we're doing blocking I/O.
                launch(Dispatchers.IO) {
                    val bufferedImage = ImageIO.read(file)
                    val scaledImage = getResizedImage(bufferedImage)
                    val outputFile = File(file.absolutePath)
                    ImageIO.write(scaledImage, file.extension, outputFile)
                }
            }
        }
    }
}

suspend fun saveImage(
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

// WithContext suspends the current coroutine and runs its block in the specified coroutine context. Once the block is complete this coroutine resumes.
// The IO dispatcher is designed for blocking I/O jobs. It shares a threadpool with the Default dispatcher but spawns new threads if none are available.
private suspend fun writeImage(
        url: String,
        path: String
) = withContext(Dispatchers.IO) {
    val readableByteChannel: ReadableByteChannel = Channels.newChannel(URL(url).openStream())

    val fileOutputStream = FileOutputStream(path)
    val fileChannel: FileChannel = fileOutputStream.channel
    fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE)
}

private suspend fun getResizedImage(
        img: BufferedImage
): BufferedImage = withContext(Dispatchers.IO) {
    val dimension = getScaledDimension(img)
    val tmp = img.getScaledInstance(dimension.width, dimension.height, Image.SCALE_SMOOTH)
    val bufferedImage = BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB)
    bufferedImage.createGraphics().run {
        drawImage(tmp, 0, 0, null)
        dispose()
    }
    return@withContext bufferedImage
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