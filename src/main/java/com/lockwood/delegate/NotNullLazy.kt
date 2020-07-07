package com.lockwood.delegate

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class NotNullLazy<T> : ReadWriteProperty<Any?, T> {

    private var notNullValue: T? = null

    @Throws(IllegalStateException::class)
    override fun getValue(
            thisRef: Any?,
            property: KProperty<*>
    ): T = notNullValue ?: throw IllegalStateException("${property.name} not init")

    @Throws(IllegalStateException::class)
    override fun setValue(
            thisRef: Any?,
            property: KProperty<*>,
            value: T
    ) {
        notNullValue = if (notNullValue == null) {
            value
        } else {
            throw IllegalStateException("${property.name} already init")
        }
    }

}

fun <T> notNullLazy(): NotNullLazy<T> = NotNullLazy()