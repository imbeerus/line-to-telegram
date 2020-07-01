package com.lockwood.extensions

import org.jsoup.nodes.Element
import org.jsoup.select.Elements

private const val STATIC_IMAGE_TYPE = "static"
private const val ANIMATION_IMAGE_TYPE = "animation"

private const val IMAGE_TYPE_INDEX = 0
private const val STATIC_URL_INDEX = 2
private const val ANIMATION_URL_INDEX = 4

private const val DATA_PREVIEW_SEPARATOR = ","
private const val DATA_PREVIEW_ARGUMENT_SEPARATOR = " : "

val Element.dataPreview: String
    get() = attr("data-preview")

val Elements.liElements: Elements
    get() = select("li")

fun String.parseImageLink(): String {
    val stickerDataValues = split(DATA_PREVIEW_SEPARATOR)

    val imageType = stickerDataValues[IMAGE_TYPE_INDEX].extractValue()

    val staticImageUrl = stickerDataValues[STATIC_URL_INDEX].extractValue()
    val animationImageUrl = stickerDataValues[ANIMATION_URL_INDEX].extractValue()

    return if (imageType != ANIMATION_IMAGE_TYPE) {
        staticImageUrl
    } else {
        animationImageUrl
    }
}

private fun String.extractValue(): String {
    val argAndValue = split(DATA_PREVIEW_ARGUMENT_SEPARATOR)

    return argAndValue[1].replace("\"", "")
}