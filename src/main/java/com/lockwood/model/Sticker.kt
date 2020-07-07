package com.lockwood.model

class Sticker(
        val id: Int,
        val url: String,
        val imageFormat: String,
        val name: String = id.toString(),
        val packTitle: String
) {

    override fun toString(): String {
        return name
    }

}