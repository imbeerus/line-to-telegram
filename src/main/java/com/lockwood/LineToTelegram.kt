package com.lockwood

import com.lockwood.executor.pack.StickerPackExecutor
import com.lockwood.executor.sticker.StickerPackImageSaver
import com.lockwood.executor.sticker.TelegramImageConverter
import com.lockwood.extensions.*
import com.lockwood.model.StickerPack
import com.lockwood.parser.LineStickerPackParser
import java.util.concurrent.ExecutionException

@ExperimentalStdlibApi
fun main(
        args: Array<String>
) {

    //region Show no args message
    val isArgsEmpty = args.isNullOrEmpty()

    if (isArgsEmpty) {
        printNoArgsMessage()
        return
    }
    //endregion

    //region Fields
    val lineStickerPackParser = LineStickerPackParser()
    val failedLinks = mutableListOf<String>()
    //endregion

    //region Setup Executors
    ioExecutor = newIOExecutor(args.size)
    networkExecutor = newNetworkExecutor(args.size)
    //endregion

    //region Read args and start parsing given links
    args.forEach { link ->
        if (lineStickerPackParser.isValidLink(link)) {

            //region Show start message
            printStartMessage(link)
            //endregion

            //region Start parse Sticker Pack
            val stickerPack: StickerPack

            try {
                stickerPack = StickerPackExecutor(link = link, parser = lineStickerPackParser).get()
            } catch (e: ExecutionException) {
                failedLinks.add(link)
                return@forEach
            }
            //endregion

            //region Start parse Sticker Pack
            try {
                StickerPackImageSaver(stickerPack = stickerPack).execute()
            } catch (e: ExecutionException) {
                failedLinks.add(link)
                return@forEach
            }
            //endregion

            //region Start prepare Sticker Images for Telegram
            try {
                TelegramImageConverter(folderName = stickerPack.title, isAnimated = stickerPack.isAnimated).execute()
            } catch (e: ExecutionException) {
                failedLinks.add(link)
                return@forEach
            }
            //endregion
        }
    }
    //endregion

    //region Shutdown executors
    ioExecutor.shutdown()
    networkExecutor.shutdown()
    //endregion

    awaitTermination(ioExecutor, networkExecutor) {
        //region Show result message
        val isFullSuccess = failedLinks.isEmpty()
        val isHasFails = failedLinks.isNotEmpty()

        val currentDirectory = System.getProperty("user.dir")

        printDownloadSuccessMessage()
        printDownloadPathMessage(currentDirectory)

        if (isFullSuccess) {
            return
        }

        if (isHasFails) {
            printDownloadFailedMessage(failedLinks)
        }
        //endregion
    }

}