package com.cottacush.android.hiddencam

import android.content.Context
import android.util.Log
import android.util.Rational
import android.util.Size
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureConfig
import java.io.File


class HiddenCam(private val context: Context, private val baseFileDirectory: File) {

    private val imageCapture: ImageCapture
    private val imageCaptureConfig: ImageCaptureConfig

    companion object {
        private const val TAG = "HiddenCam"
    }


    fun start() {

    }

    fun pause() {

    }

    fun destroy() {

    }

    init {
        val displaySize = getDefaultDisplaySize(context)
        imageCaptureConfig = ImageCaptureConfig.Builder()
            .apply {
                setTargetResolution(Size(displaySize.x, displaySize.y))
                setTargetAspectRatio(
                    Rational(
                        displaySize.x,
                        displaySize.y
                    )
                ) // TODO make this configurable ?
                setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)

                //TODO make other necessary configurations, to get best picture

            }.build()

        imageCapture = ImageCapture(imageCaptureConfig)
    }


    fun capture() {
        imageCapture.takePicture(
            createFile(baseFileDirectory),
            object : ImageCapture.OnImageSavedListener {
                override fun onError(
                    imageCaptureError: ImageCapture.ImageCaptureError,
                    message: String,
                    cause: Throwable?
                ) {
                    Log.e(TAG, "Photo capture failed: $message")
                    cause?.printStackTrace()
                }

                override fun onImageSaved(file: File) {
                    val msg = "Photo capture succeeded: ${file.absolutePath}"
                    Log.d(TAG, msg)
                }
            })
    }


}

