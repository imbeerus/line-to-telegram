package com.lockwood.constants

import java.io.File

object Desktop {

    val WORKING_DIRECTORY = System.getProperty("user.dir")

    val STICKERS_DIRECTORY = "$WORKING_DIRECTORY${File.separator}Stickers"

}