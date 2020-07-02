package com.lockwood

import com.lockwood.constants.Desktop
import com.lockwood.constants.LineWeb
import com.lockwood.executor.pack.StickerPackExecutor
import com.lockwood.executor.sticker.StickerPackImageSaver
import com.lockwood.extensions.*
import com.lockwood.model.StickerPack
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
    val failedLinks = mutableListOf<String>()
    //endregion

    //region Setup Executors
    ioExecutor = newIOExecutor(args.size)
    networkExecutor = newNetworkExecutor(args.size)
    //endregion

    //region Read args and start parsing given links
    args.forEach { link ->
        val isValidLink = link.startsWith(LineWeb.STICKER_SHOP_URL)

        if (isValidLink) {

            //region Show start message
            printStartMessage(link)
            //endregion

            //region Start parse Sticker Pack
            val stickerPack: StickerPack

            try {
                stickerPack = StickerPackExecutor(stickerPageLink = link).get()
            } catch (e: ExecutionException) {
                failedLinks.add(link)
                return@forEach
            }

            //endregion

            try {
                StickerPackImageSaver(stickerPack = stickerPack).execute()
            } catch (e: ExecutionException) {
                failedLinks.add(link)
                return@forEach
            }
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

        val currentDirectory = Desktop.WORKING_DIRECTORY

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