package com.lockwood.parser

import com.lockwood.model.StickerPack
import org.jsoup.nodes.Element

abstract class StickerPackParser {

    abstract fun isValidLink(link: String): Boolean

    abstract suspend fun parseStickerPack(link: String): StickerPack

    abstract fun selectPackTitle(page: Element): String

}