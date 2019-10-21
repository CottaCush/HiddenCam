package com.cottacush.android.hiddencam

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Point
import android.util.Rational
import android.util.Size
import android.view.WindowManager
import androidx.core.content.ContextCompat
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private const val DEFAULT_FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
private const val DEFAULT_PHOTO_EXTENSION = ".jpg"

internal fun getDefaultDisplaySize(context: Context): Point {
    val display =
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
    val size = Point()
    display.getSize(size)
    return size
}

internal fun createFile(
    baseDirectory: File
): File = File(
    baseDirectory, SimpleDateFormat(DEFAULT_FILENAME, Locale.US)
        .format(System.currentTimeMillis()) + DEFAULT_PHOTO_EXTENSION
)//TODO be sure to tell the library users to cleanup the files in the directory provided

internal fun getDefaultScreenResoultion(context: Context): Size {
    val displaySize = getDefaultDisplaySize(context)
    return Size(displaySize.x, displaySize.y)
}

internal fun getDefaultAspectRatio(context: Context): Rational {
    val displaySize = getDefaultDisplaySize(context)
    return Rational(displaySize.x, displaySize.y)
}

private val requiredPermissions =
    mutableListOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ).toTypedArray()

internal fun Context.hasPermissions(): Boolean {
    for (permission in requiredPermissions) {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) return false
    }
    return true
}