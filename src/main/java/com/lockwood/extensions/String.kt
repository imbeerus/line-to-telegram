package com.lockwood.extensions

private const val INVALID_CHARACTERS_REGEX = "[\\\\/:*?\"<>|]"

private const val PRODUCT_IMAGE_REGEX = "\\d{2,}"

fun String.parseId(): Int {
    val idRegex = PRODUCT_IMAGE_REGEX.toRegex()
    val id = idRegex.find(this)?.value?.toInt()

    return id ?: -1
}

fun String.removeFileNameInvalidCharacters(): String {
    val invalidCharactersRegex = INVALID_CHARACTERS_REGEX.toRegex()
    return replace(invalidCharactersRegex, "")
}