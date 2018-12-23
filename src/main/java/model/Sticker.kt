package model

class Sticker(val url: String) {
    val name:String
    get() {
        val leftIndent = "sticker/"
        val rightIndent = "/ANDROID"
        return url.substring(url.indexOf(leftIndent) + leftIndent.length, url.indexOf(rightIndent))
    }
}