package utils

import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import javax.imageio.ImageIO


object Utils {
    private val FONT_SIZE = 48
    private val STICKER_SIZE = 512
    private val WORKING_DIRECTORY = System.getProperty("user.dir")
    private val DEFAULT_FOLDER_NAME = "stickers"

    fun saveImage(imageUrl: String, name: String, dir: String) {
        val pathName = "$WORKING_DIRECTORY//$dir//$name.png"
        val url = URL(imageUrl)
        writeToFile(url, pathName)
    }

    fun saveImage(imageUrl: String, name: String) {
        saveImage(imageUrl, name, DEFAULT_FOLDER_NAME)
    }

    fun writeCaptionImage(caption: String, dir: String) {
        val fontSize = when {
            caption.length > 40 -> FONT_SIZE / 4
            caption.length > 20 -> FONT_SIZE / 2
            else -> FONT_SIZE
        }
        val font = Font("Roboto", Font.PLAIN, fontSize)
        val fontDimension = Dimension(STICKER_SIZE, STICKER_SIZE)
        val image = createCaptionImage(caption, font, fontDimension)
        ImageIO.write(image, "png", File("$WORKING_DIRECTORY//$dir//copyright.png"))
    }

    fun writeCaptionImage(str: String) {
        writeCaptionImage(str, DEFAULT_FOLDER_NAME)
    }

    fun createDirectory(name: String) {
        val dir = File(name)
        dir.mkdir()
    }

    private fun createCaptionImage(str: String, font: Font, dimension: Dimension): BufferedImage {
        val img = BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB)

        val g2d = img.createGraphics()
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY)
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE)
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)
        g2d.font = font

        val fm = g2d.fontMetrics
        g2d.color = Color.BLACK

        val lines = str.split("\n")
        val x = (STICKER_SIZE - fm.stringWidth(lines[lines.size - 1])) / 2
        var y = (STICKER_SIZE / lines.size) - fm.ascent / lines.size
        for (line in lines) {
            g2d.drawString(line, x, y)
            y += fm.height
        }

        g2d.dispose()
        return img
    }

    private fun writeToFile(url: URL, pathName: String) {
        val bufferedImage = ImageIO.read(url)
        val scaledImage = getResizedImage(bufferedImage)
        val outputFile = File(pathName)
        ImageIO.write(scaledImage, "png", outputFile)
    }

    private fun getResizedImage(img: BufferedImage): BufferedImage {
        val dimension = getScaledDimension(img)
        val tmp = img.getScaledInstance(dimension.width, dimension.height, Image.SCALE_SMOOTH)
        val dimg = BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB)
        val g2d = dimg.createGraphics()
        g2d.drawImage(tmp, 0, 0, null)
        g2d.dispose()
        return dimg
    }

    private fun getScaledDimension(img: BufferedImage): Dimension {
        val width = img.width
        val height = img.height
        val boundWidth = STICKER_SIZE
        val boundHeight = STICKER_SIZE
        var newWidth = width
        var newHeight = height

        if (width != boundWidth) {
            newWidth = boundWidth
            newHeight = newWidth * height / width
        }
        if (newHeight > boundHeight) {
            newHeight = boundHeight
            newWidth = newHeight * width / height
        }
        return Dimension(newWidth, newHeight)
    }
}