package com.lockwood.extensions

private const val PRODUCT_IMAGE_REGEX = "\\d{2,}"

fun String.parseProductId(): Int {
    val idRegex = PRODUCT_IMAGE_REGEX.toRegex()
    val productId = idRegex.find(this)?.value?.toInt()

    return productId ?: -1
}