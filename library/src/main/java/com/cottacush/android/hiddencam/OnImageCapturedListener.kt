package com.cottacush.android.hiddencam

import java.io.File

interface OnImageCapturedListener {
    fun onImageCaptured(image: File)
    fun onError(e: Throwable)
}