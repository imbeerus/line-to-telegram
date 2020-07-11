import com.lockwood.extensions.*
import com.lockwood.model.Sticker
import com.lockwood.model.StickerPack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

@Throws(IllegalStateException::class)
suspend fun parseStickerPack(
    link: String
): StickerPack {
    // WithContext suspends the current coroutine and runs its block in the specified coroutine context. Once the block is complete this coroutine resumes.
    // The IO dispatcher is designed for blocking I/O jobs. It shares a threadpool with the Default dispatcher but spawns new threads if none are available.
    val page = withContext(Dispatchers.IO) {
        Jsoup.connect(link).get().body()
    }

    val packTitle = selectPackTitle(page)
    val packCopyright = selectPackCopyright(page)

    val stickers = selectStickersImageList(page).map {
        val stickerId = it.parseProductId()
        Sticker(stickerId, it, packTitle = packTitle)
    }.toTypedArray()

    return StickerPack(packTitle, packCopyright, stickers)
}

fun selectStickersImageList(
    page: Element
): List<String> {
    val liImagesList = selectLiImageElements(page)

    checkNotNull(liImagesList) {
        return emptyList()
    }

    // From each li element select span that contains img url in data-preview
    return liImagesList.map { it.dataPreview.parseImageLink() }
}

@Throws(IllegalStateException::class)
fun selectPackTitle(
    page: Element
): String {
    val titleParagraph = page.packTitleParagraph
    val title = titleParagraph.text()

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