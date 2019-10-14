package com.cottacush.android.hiddencam

import android.content.Context
import android.util.Log
import android.util.Rational
import android.util.Size
import androidx.camera.core.CameraX
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureConfig
import java.io.File

//TODO remove all Logs once we are done?.

class HiddenCam(private val context: Context, private val baseFileDirectory: File) {

    lateinit var imageCapture: ImageCapture
    private lateinit var imageCaptureConfig: ImageCaptureConfig
    private val lifeCycleOwner = HiddenCamLifeCycleOwner()

    private val displaySize = getDefaultDisplaySize(context)



    companion object {
        private const val TAG = "HiddenCam"
    }



    //TODO implement a builder pattern and to get information for ImageCaptureConfig



    //Start: -- Cam Engine life cycle
    fun start() {
        imageCapture = ImageCapture(imageCaptureConfig)
        lifeCycleOwner.start()
    }


    fun destroy() {
        lifeCycleOwner.tearDown()
    }


    //End: -- Cam Engine life cycle


    init {

        //TODO set default for the properties needed to configure
        // ImageCaptureConfig.Builder()
        aspectRatio = Rational(displaySize.x, displaySize.y)

    }


    private fun buildImageCaptureConfig() {
        imageCaptureConfig = ImageCaptureConfig.Builder()
            .apply {
                setTargetResolution(cameraResolution)
                setTargetAspectRatio(aspectRatio) // TODO make this configurable ?
                setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)// TODO experiment with all possible capture modes to see which one gives best picture
                //TODO make other necessary configurations, to get best picture

            }.build()

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
                    //TODO report back to the listener
                    Log.e(TAG, "Photo capture failed: $message")
                    cause?.printStackTrace()
                }

                override fun onImageSaved(file: File) {
                    val msg = "Photo capture succeeded: ${file.absolutePath}"
                    //TODO report back to the listener
                    Log.d(TAG, msg)
                }
            })
    }
}

