package com.lockwood

import com.lockwood.constants.Desktop
import com.lockwood.constants.LineWeb
import com.lockwood.extensions.*
import com.lockwood.model.StickerPack
import kotlinx.coroutines.*
import parseStickerPack
import java.lang.IllegalStateException
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

    //region Read args and start parsing given links

    // runBlocking blocks the main thread and essentially behaves like a coroutineScope (see LineWeb.saveStickersInPack() for more info)
    // The default dispatcher schedules coroutines on a thread pool with a number of threads equal to the number of CPU cores on the machine.
    // It is designed for running CPU-intensive workloads.
    runBlocking(Dispatchers.Default) {
        val failedLinks = mutableListOf<Deferred<String?>>()
        args.forEach { link ->
            val isValidLink = link.startsWith(LineWeb.STICKER_SHOP_URL)

            if (isValidLink) {

                //region Show start message
                printStartMessage(link)
                //endregion

                //region Start parse Sticker Pack

                // Async launches a coroutine which will go and run in another thread from the Default dispatcher.
                // The result of the coroutine can be fetched via .await()-ing the return value from async()
                // This is different from other async libraries, as coroutines are sequential by default and require you to opt-in to launching concurrent jobs
                failedLinks += async {
                    val stickerPack: StickerPack = try {
                        // This is a suspending function call. Invoking this suspends the current coroutine and resumes it when the work is complete.
                        parseStickerPack(link)
                    } catch (e: IllegalStateException) {
                        return@async link
                    }

                    try {
                        // Another suspending function call.
                        LineWeb.saveStickersInPack(stickerPack)
                    } catch (e: ExecutionException) {
                        return@async link
                    }

                    return@async null
                }
                //endregion
            }
        }

        //region Show result message
        // awaitAll() will wait for all the async jobs to complete and return the results from all of them
        val allFailedLinks = failedLinks.awaitAll().filterNotNull()
        val isFullSuccess = allFailedLinks.isEmpty()

        val currentDirectory = Desktop.WORKING_DIRECTORY

        printDownloadSuccessMessage()
        printDownloadPathMessage(currentDirectory)

        if (isFullSuccess) {
            return@runBlocking
        } else {
            printDownloadFailedMessage(allFailedLinks)
        }
        //endregion
    }
    //endregion
}