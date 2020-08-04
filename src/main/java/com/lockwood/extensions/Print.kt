package com.lockwood.extensions

private const val PROJECT_URL = "https://github.com/lndmflngs/line-to-telegram"
private const val STICKERS_BOT_URL = "https://t.me/stickers"

private const val START_DOWNLOAD_CAPTION = "Start parsing %s"
private const val PATH_DOWNLOAD_CAPTION = "Your stickers downloaded to %s"
private const val NOTHING_TO_DOWNLOAD_CAPTION = "You didn't pass any arguments to program"

private const val CURRENT_THREAD_CAPTION = "Current Thread[%s]: %s"

private const val TELEGRAM_BOT_MESSAGE =
    "Now you can use @Stickers bot ($STICKERS_BOT_URL) at Telegram to create sticker packs"

private const val ADD_ISSUE_MESSAGE = "There are some troubles while parsing\n" +
        "Check that sticker packs available for your region\n" +
        "If it is, add issue to $PROJECT_URL/issues with you sticker packs links\n" +
        "Links: %s"

fun printStartMessage(
    stickerPackLink: String
) {
    val message = START_DOWNLOAD_CAPTION.format(stickerPackLink)
    println(message)
}

fun printDownloadFailedMessage(
    links: List<String>
) {
    val linksCaption = links.toString()

    val message = ADD_ISSUE_MESSAGE.format(linksCaption)
    println()
    println(message)
}

fun printCurrentThread() {
    val message = CURRENT_THREAD_CAPTION.format(currentTime, currentThreadName)
    println(message)
}

fun printDownloadPathMessage(
    path: String
) {
    val message = PATH_DOWNLOAD_CAPTION.format(path)
    println(message)
}

fun printNoArgsMessage() {
    println(NOTHING_TO_DOWNLOAD_CAPTION)
}

fun printDownloadSuccessMessage() {
    println()
    println(TELEGRAM_BOT_MESSAGE)
}