package com.cottacush.android.hiddencam

import android.content.Context
import android.util.Log
import android.util.Rational
import android.util.Size
import androidx.camera.core.CameraX
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureConfig
import com.cottacush.android.hiddencam.CaptureTimeFrequency.*
import java.io.File

class HiddenCam(
    private val context: Context, private val baseFileDirectory: File,
    private val imageCapturedListener: OnImageCapturedListener,
    private val captureFrequency: CaptureTimeFrequency = OneShot,
    private val aspectRatio: Rational = getDefaultAspectRatio(context),
    private val cameraResolution: Size = getDefaultScreenResolution(context),
    private val cameraFacingDirection: CameraX.LensFacing = CameraX.LensFacing.FRONT
) {
    private lateinit var captureTimer: CaptureTimerHandler
    private val lifeCycleOwner = HiddenCamLifeCycleOwner()
    private var imageCapture: ImageCapture
    private var imageCaptureConfig: ImageCaptureConfig = ImageCaptureConfig.Builder()
        .apply {
            setLensFacing(cameraFacingDirection)
            //  setTargetResolution(cameraResolution)
            setTargetAspectRatio(aspectRatio)
            setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
        }.build()

    init {
        if (context.hasPermissions()) {
            imageCapture = ImageCapture(imageCaptureConfig)
            CameraX.bindToLifecycle(lifeCycleOwner, imageCapture)
            when (val interval = captureFrequency) {
                OneShot -> {
                    //Nothing for now, we don't need to schedule anything
                }
                is Recurring -> {
                    captureTimer = CaptureTimerHandler(
                        interval.captureIntervalMillis,
                        object : CaptureTimeListener {
                            override fun onCaptureTimeTick() {
                                capture()
                            }
                        })
                }
            }

        } else throw SecurityException("You need to have access to both CAMERA and WRITE_EXTERNAL_STORAGE permissions")
    }

    //Start: -- Cam Engine life cycle
    fun start() {
        lifeCycleOwner.start()
        if (captureFrequency is Recurring)
            captureTimer.startUpdates()
    }

    fun stop() {
        lifeCycleOwner.stop()
        if (captureFrequency is Recurring)
            captureTimer.stopUpdates()
    }

    fun destroy() {
        lifeCycleOwner.tearDown()
        if (captureFrequency is Recurring)
            captureTimer.stopUpdates()
    }

    //End: -- Cam Engine life cycle
    private fun capture() {
        imageCapture.takePicture(createFile(baseFileDirectory),
            object : ImageCapture.OnImageSavedListener {
                override fun onError(
                    imageCaptureError: ImageCapture.ImageCaptureError,
                    message: String,
                    cause: Throwable?
                ) {
                    Log.e(TAG, "Photo capture failed: $message")
                    imageCapturedListener.onImageCaptureError(cause)
                }

                override fun onImageSaved(file: File) {
                    Log.d(TAG, "Photo capture succeeded: ${file.absolutePath}")
                    imageCapturedListener.onImageCaptured(file)
                }
            })
    }

    public fun captureImage() {
        if (captureFrequency is OneShot)
            capture()
    }

    companion object {
        private const val TAG = "HIDDEN_CAM"
    }
}

