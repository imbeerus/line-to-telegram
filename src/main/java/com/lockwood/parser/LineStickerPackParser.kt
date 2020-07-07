package com.lockwood.parser

import com.lockwood.extensions.parseId
import com.lockwood.extensions.removeFileNameInvalidCharacters
import com.lockwood.model.Sticker
import com.lockwood.model.StickerPack
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

@ExperimentalStdlibApi
class LineStickerPackParser : StickerPackParser() {

    companion object {

        private const val STICKER_SHOP_URL = "https://store.line.me/stickershop"

        private const val STATIC_IMAGE_TYPE = "static"
        private const val ANIMATION_IMAGE_TYPE = "animation"

        private const val DEFAULT_IMAGE_FORMAT = "png"
    }

    override fun isValidLink(link: String): Boolean = link.startsWith(STICKER_SHOP_URL)

    @Throws(IllegalStateException::class)
    override fun parseStickerPack(link: String): StickerPack {
        val page = Jsoup.connect(link).get().body()

        val packTitle = selectPackTitle(page)
        val packCopyright = selectPackCopyright(page)

        val (imageLinks: List<String>, isAnimated: Boolean) = selectStickersImageList(page).run { first to second }

        val stickers = imageLinks.map {
            val stickerId = it.parseId()

            Sticker(stickerId, it, packTitle = packTitle, imageFormat = DEFAULT_IMAGE_FORMAT)
        }.toTypedArray()

        return StickerPack(packTitle, packCopyright, isAnimated, stickers)
    }

    @Throws(IllegalStateException::class)
    override fun selectPackTitle(page: Element): String {
        val titleParagraph = page.select("p.mdCMN38Item01Ttl")
        val title = titleParagraph.text().removeFileNameInvalidCharacters()

        return if (title.isEmpty()) {
            throw IllegalStateException("There is no title to parse, probably sticker pack not available for your region")
        } else {
            title
        }
    }

    private fun selectStickersImageList(page: Element): Pair<List<String>, Boolean> {
        val liImagesList = selectLiImageElements(page)

        checkNotNull(liImagesList) {
            return Pair(emptyList(), false)
        }

        // Check is pack contains animated stickers or not from data-preview of first sticker
        val isAnimated = liImagesList.first().attr("data-preview").isAnimatedType()

        // From each li element select span that contains img url in data-preview
        val imageLinks = buildList {

            liImagesList.forEach {
                val imageLink = it.attr("data-preview").parseImageLink()
                add(imageLink)
            }
        }

        return Pair(imageLinks, isAnimated)
    }

    private fun selectPackCopyright(page: Element): String {
        val copyrightParagraph = page.select("p.mdCMN09Copy")
        val copyright = copyrightParagraph.text()

        return if (copyright.isEmpty()) {
            "Unknown"
        } else {
            copyright
        }
    }

    private fun selectLiImageElements(page: Element): Elements? {
        return try {
            val stickersUl = page.select("ul.FnStickerList")
            stickersUl.select("li")
        } catch (e: IllegalStateException) {
            null
        }
    }

    private fun String.isAnimatedType(): Boolean {
        val stickerDataValues = split(",")
        val imageType = stickerDataValues[0].extractValue()

        return imageType == ANIMATION_IMAGE_TYPE
    }


    private fun String.parseImageLink(): String {
        val stickerDataValues = split(",")

        val staticImageUrl = stickerDataValues[2].extractValue()
        val animationImageUrl = stickerDataValues[4].extractValue()

        return if (!isAnimatedType()) {
            staticImageUrl
        } else {
            animationImageUrl
        }
    }

    private fun String.extractValue(): String {
        val argAndValue = split(" : ")
        return argAndValue[1].replace("\"", "")
    }

}