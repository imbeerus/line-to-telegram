import com.lockwood.model.Pack
import com.lockwood.utils.Utils

private const val FIVE_SECOND = 5000L
private const val STICKER_SHOP_URL = "https://store.line.me/stickershop"

fun main(args: Array<String>) {

    var isSuccess = false

    args.forEach { link ->
        if (link.startsWith(STICKER_SHOP_URL)) {
            println("Start parsing $link...")
            val stickerPack = Pack(link)
            val stickers = stickerPack.stickers

            if (!stickers.isNullOrEmpty()) {
                isSuccess = true
                Utils.createDirectory(stickerPack.title)
                stickers.forEach { Utils.saveImage(it.url, it.name, stickerPack.title) }
                Utils.writeCaptionImage(stickerPack.copyright, stickerPack.title)
                println("${stickerPack.title} pack is ready")
                Thread.sleep(FIVE_SECOND)

            }
        }
    }

    if (isSuccess) {
        println("Now you can use @stickers at Telegram to create sticker packs")
    } else {
        println(
                "There are some troubles while parsing stickers\n" +
                        "Add issue to https://github.com/lndmflngs/line-to-telegram/issues with you sticker pack link\n" +
                        "Links: ${args.map { it }}"
        )
    }

}