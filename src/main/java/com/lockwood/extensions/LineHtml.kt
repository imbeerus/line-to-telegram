package com.lockwood.extensions

import org.jsoup.nodes.Element
import org.jsoup.select.Elements

val Element.stickersUlElements: Elements
    get() = select("ul.FnStickerList")

val Element.packTitleParagraph: Elements
    get() = select("p.mdCMN38Item01Ttl")

val Element.copyrightParagraph: Elements
    get() = select("p.mdCMN09Copy")