package com.lockwood.executor

import java.util.concurrent.Future

interface DataExecutor<T : Any> {

    fun submit(): Future<T>

    fun get(): T

}