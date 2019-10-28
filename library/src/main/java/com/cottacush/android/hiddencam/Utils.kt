package com.cottacush.android.hiddencam

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Point
import android.util.Log
import android.util.Rational
import android.util.Size
import android.view.WindowManager
import androidx.core.content.ContextCompat
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private const val DEFAULT_FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
private const val DEFAULT_PHOTO_EXTENSION = ".jpg"
private val requiredPermissions =
    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

class HiddenCamUtils {
    companion object {
        fun getAspectRatioWithScreenDimension(context: Context): Rational =
            context.getDefaultAspectRatio()
    }
}

internal fun Context.getDefaultDisplaySize(): Point {
    val display = getTypedSystemService<WindowManager>(Context.WINDOW_SERVICE).defaultDisplay
    val size = Point()
    display.getSize(size)
    return size
}

internal fun Context.getDefaultRotation(): Int =
    getTypedSystemService<WindowManager>(Context.WINDOW_SERVICE).defaultDisplay.rotation


inline fun <reified T> Context.getTypedSystemService(name: String): T {
    return getSystemService(name) as T
}

internal fun createFile(
    baseDirectory: File
): File = File(
    baseDirectory, SimpleDateFormat(DEFAULT_FILENAME, Locale.US)
        .format(System.currentTimeMillis()) + DEFAULT_PHOTO_EXTENSION

)

internal fun Context.getDefaultAspectRatio(): Rational {
    val displaySize = getDefaultDisplaySize()
    return Rational(displaySize.x, displaySize.y)
}

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
