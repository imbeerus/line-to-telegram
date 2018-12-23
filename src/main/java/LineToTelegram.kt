import model.Pack
import utils.Utils

private const val FIVE_SECOND = 5000L
private const val STICKER_SHOP_URL = "https://store.line.me/stickershop"

fun main(args: Array<String>) {
    args.forEach { link ->
        if (link.startsWith(STICKER_SHOP_URL)) {
            val stickerPack = Pack(link)
            Utils.createDirectory(stickerPack.title)
            stickerPack.stickers.forEach { Utils.saveImage(it.url, it.name, stickerPack.title) }
            Utils.writeCaptionImage(stickerPack.copyright, stickerPack.title)
            println("${stickerPack.title} pack is ready")
            Thread.sleep(FIVE_SECOND)
        }
    }
    println("Now you can use @stickers at Telegram to create sticker packs")
}