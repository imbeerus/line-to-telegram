package model

import org.jsoup.Jsoup
import utils.JsoupUtils

class Pack(packUrl: String) {

    private val doc = Jsoup.connect(packUrl).get().body()

    val copyright: String = JsoupUtils.getCopyrightCaption(doc)

    val title: String = JsoupUtils.getPackTitle(doc)

    val stickers: Array<Sticker>
        get() {
            val links = JsoupUtils.getImageLinks(doc)
            val stickers = arrayListOf<Sticker>()
            links.forEach { stickers.add(Sticker(it)) }
            return stickers.toTypedArray()
        }

}