package com.cottacush.android.hiddencam

import android.util.Log
import android.util.Size
import android.widget.ImageButton
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureConfig
import java.io.File


class HiddenCam {

    private val imageCapture: ImageCapture
    private val imageCaptureConfig: ImageCaptureConfig


    init {
        val displaySize = getDefaultDisplaySize(this)
        imageCaptureConfig = ImageCaptureConfig.Builder()
            .apply {
                setTargetResolution(Size(displaySize.x, displaySize.y))
                setTargetAspectRatio(AspectRatio.RATIO_16_9) // TODO make this configurable ?
                setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)

                //TODO make other necessary configurations, to get best picture

            }.build()

        imageCapture = ImageCapture(imageCaptureConfig)
    }


    fun capture() {
        val file = File(
            externalMediaDirs.first(),
            "${System.currentTimeMillis()}.jpg"
        )
        imageCapture.takePicture(file, object : ImageCapture.OnImageSavedListener {
            override fun onError(
                error: ImageCapture.UseCaseError,
                message: String, exc: Throwable?
            ) {
                val msg = "Photo capture failed: $message"
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                Log.e("CameraXApp", msg)
                exc?.printStackTrace()
            }

            override fun onImageSaved(file: File) {
                val msg = "Photo capture succeeded: ${file.absolutePath}"
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                Log.d("CameraXApp", msg)
            }
        })
    }
}


/*
Lifeccyles events to watch {

   // Lifecycle.Event.ON_START
   // Lifecycle.Event.ON_STOP
   // Lifecycle.Event.ON_DESTROY

}*/
