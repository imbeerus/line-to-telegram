package com.lockwood.extensions

import java.awt.Image
import java.awt.image.BufferedImage

fun BufferedImage.getScaledInstance(
    resultSize: Int
): BufferedImage {
    val sizePair = getScaledSizePair(resultSize)
    val (width: Int, height: Int) = sizePair.run { first to second }

    val tmp = getScaledInstance(width, height, Image.SCALE_SMOOTH)
    val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

    bufferedImage.createGraphics().run {
        drawImage(tmp, 0, 0, null)
        dispose()
    }

    return bufferedImage
}

fun BufferedImage.getScaledSizePair(
    resultSize: Int
): Pair<Int, Int> {
    val width = width
    val height = height

    var newWidth = width
    var newHeight = height

    if (width != resultSize) {
        newWidth = resultSize
        newHeight = newWidth * height / width
    }

    if (newHeight > resultSize) {
        newHeight = resultSize
        newWidth = newHeight * width / height
    }

    return Pair(newWidth, newHeight)
}