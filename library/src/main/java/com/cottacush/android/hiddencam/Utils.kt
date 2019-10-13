package com.cottacush.android.hiddencam

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private const val DEFAULT_FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
private const val PHOTO_EXTENSION = ".jpg"


fun getDefaultDisplaySize(context: Context): Point {
    val display =
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
    val size = Point()
    display.getSize(size)
    return size
}

private fun createFile(
    baseFolder: File,
    format: String = DEFAULT_FILENAME,
    extension: String = PHOTO_EXTENSION
) = File(baseFolder, SimpleDateFormat(format, Locale.US).format(System.currentTimeMillis()) + extension)