package com.lockwood

import com.lockwood.extensions.convertImagesInFolderForTelegram
import com.lockwood.extensions.saveStickers
import com.lockwood.extensions.*
import com.lockwood.parser.LineStickerPackParser
import kotlinx.coroutines.*
import java.util.concurrent.ExecutionException

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

    // runBlocking blocks the main thread and essentially behaves like a coroutineScope (see LineWeb.saveStickersInPack() for more info)
    // The default dispatcher schedules coroutines on a thread pool with a number of threads equal to the number of CPU cores on the machine.
    // It is designed for running CPU-intensive workloads.
    runBlocking(Dispatchers.Default) {
        val failedLinks = mutableListOf<Deferred<String?>>()
        args.forEach { link ->
            val lineStickerPackParser = LineStickerPackParser()
            if (lineStickerPackParser.isValidLink(link)) {

                //region Show start message
                printStartMessage(link)
                //endregion

                // Async launches a coroutine which will go and run in another thread from the Default dispatcher.
                // The result of the coroutine can be fetched via .await()-ing the return value from async()
                // This is different from other async libraries, as coroutines are sequential by default and require you to opt-in to launching concurrent jobs
                failedLinks += async {
                    //region Start parse Sticker Pack
                    val stickerPack = try {
                        lineStickerPackParser.parseStickerPack(link)
                    } catch (e: ExecutionException) {
                        return@async link
                    }
                    //endregion

                    //region Start parse Sticker Pack
                    try {
                        saveStickers(stickerPack = stickerPack)
                    } catch (e: ExecutionException) {
                        return@async link
                    }
                    //endregion

                    //region Start prepare Sticker Images for Telegram
                    try {
                        convertImagesInFolderForTelegram(folderName = stickerPack.title)
                    } catch (e: ExecutionException) {
                        return@async link
                    }
                    //endregion

                    return@async null
                }
            }
        }

        //region Show result message
        // awaitAll() will wait for all the async jobs to complete and return the results from all of them
        val allFailedLinks = failedLinks.awaitAll().filterNotNull()
        val isFullSuccess = allFailedLinks.isEmpty()

        val currentDirectory = System.getProperty("user.dir")

        printDownloadSuccessMessage()
        printDownloadPathMessage(currentDirectory)

        if (!isFullSuccess) {
            printDownloadFailedMessage(allFailedLinks)
        }
        //endregion
    }
}