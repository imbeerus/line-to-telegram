package model

import java.util.*

class Sticker(val url: String) {

    val name: String
        get() {
            val r = Regex("sticker/(\\d+)/")
            return r.find(url)?.groupValues?.get(1) ?: UUID.randomUUID().toString()
        }

}
