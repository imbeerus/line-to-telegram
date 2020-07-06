import com.lockwood.extensions.*
import com.lockwood.model.Sticker
import com.lockwood.model.StickerPack
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

@ExperimentalStdlibApi
@Throws(IllegalStateException::class)
fun parseStickerPack(
    link: String
): StickerPack {
    val page = Jsoup.connect(link).get().body()

    val packTitle = selectPackTitle(page)
    val packCopyright = selectPackCopyright(page)

    val stickers = selectStickersImageList(page).map {
        val stickerId = it.parseProductId()
        Sticker(stickerId, it, packTitle = packTitle)
    }.toTypedArray()

    return StickerPack(packTitle, packCopyright, stickers)
}

@ExperimentalStdlibApi
fun selectStickersImageList(
    page: Element
): List<String> {
    val liImagesList = selectLiImageElements(page)

    checkNotNull(liImagesList) {
        return emptyList()
    }

    // From each li element select span that contains img url in data-preview
    return buildList {

        liImagesList.forEach {
            val imageLink = it.dataPreview.parseImageLink()
            add(imageLink)
        }
    }
}

@Throws(IllegalStateException::class)
fun selectPackTitle(
    page: Element
): String {
    val titleParagraph = page.packTitleParagraph
    val title = titleParagraph.text().removeFileNameInvalidCharacters()

    return if (title.isEmpty()) {
        throw IllegalStateException("There is no title to parse, probably sticker pack not available for your region")
    } else {
        title
    }
}

fun selectPackCopyright(
    page: Element
): String {
    val copyrightParagraph = page.copyrightParagraph
    val copyright = copyrightParagraph.text()

    return if (copyright.isEmpty()) {
        "Unknown"
    } else {
        copyright
    }
}

private fun selectLiImageElements(
    page: Element
): Elements? {
    return try {
        val stickersUl = page.stickersUlElements
        stickersUl.liElements
    } catch (e: IllegalStateException) {
        null
    }
}