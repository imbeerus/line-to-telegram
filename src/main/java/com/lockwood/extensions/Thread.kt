package com.lockwood.extensions

var currentThreadName: String
    get() = Thread.currentThread().name
    private set(value) {
        Thread.currentThread().name = value
    }

fun switchThreadName(
    newThreadName: String
) {
    currentThreadName = newThreadName
}