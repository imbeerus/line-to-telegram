package model

class Sticker(val url: String) {
    val name:String
    get() {
        val r = Regex("sticker/(\\d+)/")
        return r.find(url)!!.groupValues[1]
    }
}
