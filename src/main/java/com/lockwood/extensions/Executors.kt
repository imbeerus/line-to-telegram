package com.lockwood.extensions

import com.lockwood.delegate.notNullLazy
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

var networkExecutor: ExecutorService by notNullLazy()
var ioExecutor: ExecutorService by notNullLazy()

fun newIOExecutor(
    minutes: Number
): ExecutorService {
    return ThreadPoolExecutor(
        0, 2,
        minutes.toLong(), TimeUnit.MINUTES,
        LinkedBlockingQueue()
    )
}

fun newNetworkExecutor(
    minutes: Number
): ExecutorService {
    return ThreadPoolExecutor(
        0, 5,
        minutes.toLong(), TimeUnit.MINUTES,
        LinkedBlockingQueue()
    )
}

inline fun awaitTermination(
    vararg executor: ExecutorService,
    onDone: () -> Unit = {}
) {
    val executorsCount = executor.size

    while (executor.filter(ExecutorService::isTerminated).size != executorsCount) {
        // await executors shutdown
    }

    onDone()
}